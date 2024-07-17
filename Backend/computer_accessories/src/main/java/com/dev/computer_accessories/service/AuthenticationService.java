package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.SignInRequest;
import com.dev.computer_accessories.dto.request.UserDTO;
import com.dev.computer_accessories.dto.response.TokenResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.Role;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.TokenRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.impl.RoleServiceImpl;
import com.dev.computer_accessories.token.Token;
import com.dev.computer_accessories.token.TokenType;
import com.dev.computer_accessories.util.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new ResourceNotFoundException("Incorrect username or password");
        }

        var user = userRepository.findByEmail(signInRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        var accessToken = jwtService.generateToken(user);

        var refreshToken = jwtService.generateReFreshToken(user);

        revokedAllUserTokens(user);

        savedToken(user, accessToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status(200)
                .message("Login user successful")
                .email(signInRequest.getUsername())
                .fullname(user.getFullName())
                .role(user.getRole())
                .userId(user.getId())
                .build();
    }

    public TokenResponse register(SignInRequest signInRequest) {
        if(existEmail(signInRequest.getUsername())) {
            throw new ResourceNotFoundException("Email already exists");
        }

        Role role = roleService.findByName(signInRequest.getRole().getName());

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

        if(validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public boolean existEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
