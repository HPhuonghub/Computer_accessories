package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.SignInRequest;
import com.dev.computer_accessories.dto.response.TokenResponse;
import com.dev.computer_accessories.exception.AppException;
import com.dev.computer_accessories.exception.ErrorCode;
import com.dev.computer_accessories.model.Role;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.TokenRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.impl.RoleServiceImpl;
import com.dev.computer_accessories.token.Token;
import com.dev.computer_accessories.token.TokenType;
import com.dev.computer_accessories.util.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleServiceImpl roleService;

    public TokenResponse authenticate(SignInRequest signInRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
            );
        } catch (Exception e) {
            throw new AppException(ErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
        }

        var user = userRepository.findByEmail(signInRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var accessToken = jwtService.generateToken(user);

        var refreshToken = jwtService.generateReFreshToken(user);

        revokedAllUserTokens(user);

        savedToken(user, accessToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(200)
                .build();
    }

    public TokenResponse register(SignInRequest signInRequest) {
        if (existEmail(signInRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Role role = roleService.findByName("USER");

        var user = User.builder()
                .fullName(signInRequest.getFullname())
                .email(signInRequest.getUsername())
                .password(passwordEncoder.encode(signInRequest.getPassword()))
                .status(UserStatus.valueOf("ACTIVE"))
                .role(role)
                .build();

        var savedUser = userRepository.save(user);

        var accessToken = jwtService.generateToken(user);

        var refreshToken = jwtService.generateReFreshToken(user);

        savedToken(user, accessToken);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(200)
                .message("Register user successful")
                .email(signInRequest.getUsername())
                .build();

    }

    public void savedToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false).build();

        tokenRepository.save(token);
    }

    private void revokedAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public TokenResponse refreshToken(HttpServletRequest jwtToken) {
        String token = jwtToken.getHeader("x-token");
        System.out.println("check token: " + token);

        if (StringUtils.isBlank(token)) {
            throw new AppException(ErrorCode.TOKEN_NOT_BLANK);
        }

        final String userName = jwtService.extractUsername(token);

        Optional<User> user = userRepository.findByEmail(userName);

        if (!jwtService.isValidToken(token, user.get())) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        String accessToken = jwtService.generateToken(user.get());

        savedToken(user.get(), accessToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .status(200)
                .build();
    }

    public TokenResponse logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (StringUtils.isAllBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
        return TokenResponse.builder()
                .build();
    }

    public boolean existEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
