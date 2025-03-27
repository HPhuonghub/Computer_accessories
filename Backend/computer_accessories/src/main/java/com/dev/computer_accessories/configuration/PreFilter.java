package com.dev.computer_accessories.configuration;

import com.dev.computer_accessories.exception.AppException;
import com.dev.computer_accessories.exception.ErrorCode;
import com.dev.computer_accessories.repository.TokenRepository;
import com.dev.computer_accessories.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_KEY_PREFIX = "blacklist:";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("-------------------PreFilter----------------");

//        if(request.getServletPath().startsWith("/api/v1/auth")) {
//            log.info("-----------------");
//            filterChain.doFilter(request, response);
//            return;
//        }

        final String authorization = request.getHeader("Authorization");
        log.info("Authorization: {}", authorization);

        if(StringUtils.isAllBlank(authorization) || !authorization.startsWith("Bearer ")) {
            log.error("Authorization header is missing or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authorization.substring("Bearer ".length());

        if(Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + jwt))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userEmail = jwtService.extractUsername(jwt);

        if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isValidToken(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);
    }
}
