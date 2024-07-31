package com.dev.computer_accessories.oauth2;

import com.dev.computer_accessories.auth.utils.CookieUtils;
import com.dev.computer_accessories.configuration.AppProperties;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.exception.ResourcesNotFoundException;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.oauth2.user.UserPrincipal;
import com.dev.computer_accessories.repository.TokenRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.JwtService;
import com.dev.computer_accessories.token.Token;
import com.dev.computer_accessories.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.IOException;

import static com.dev.computer_accessories.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AppProperties appProperties;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        System.out.println("in 39 success handler");
        System.out.println(targetUrl);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new ResourceNotFoundException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());


        // Get UserPrincipal from Authentication
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Get User from UserPrincipal
        User user = userRepository.findByEmailAndProvider(userPrincipal.getEmail(),userPrincipal.getProvider())
                .orElseThrow(() -> new ResourcesNotFoundException("User", "email", userPrincipal.getEmail()));

        /// Generate JWT token
        var jwtToken = jwtService.generateToken(user);
        System.out.println(jwtToken);
        var refreshToken = jwtService.generateReFreshToken(user);
        System.out.println(refreshToken);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", jwtToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // Method to save user token to database
    public void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        if (!tokenRepository.findByToken(token.getToken()).isPresent()) {
            tokenRepository.save(token);
        } else {
            log.info("Token {} already exists in database.", jwtToken);
        }
    }

    // Method to revoke all user tokens
    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}

