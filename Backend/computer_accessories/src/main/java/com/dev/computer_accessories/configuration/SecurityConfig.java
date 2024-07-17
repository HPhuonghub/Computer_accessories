package com.dev.computer_accessories.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider provider;
    private final PreFilter preFilter;
    private final String[] WHITE_LIST = {"api/v1/auth/**",
            "api/v1/user/**",
            "api/v1/product/**",
            "api/v1/supplier/**",
            "api/v1/category/**",
            "api/v1/orders/**",
            "api/v1/order-details/**",
            "/api/v1/product-specification/**"
    };


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(WHITE_LIST).permitAll()
//                        .requestMatchers(HttpMethod.GET,  "api/v1/user/list/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE,  "api/v1/user/**").hasAnyRole("ADMIN", "USER")
//                        .requestMatchers(HttpMethod.GET,  "api/v1/user/**").hasAnyRole("ADMIN", "USER")
//                        .requestMatchers(HttpMethod.POST,  "api/v1/user/**").hasAnyRole("ADMIN", "USER")
//                        .requestMatchers(HttpMethod.PUT,  "api/v1/user/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(provider).addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer ignoreResources() {
        return (webSecurity) -> webSecurity
                .ignoring()
                .requestMatchers("/actuator/**", "/v3/**", "/webjars/**", "/swagger-ui*/*swagger-initializer.js", "/swagger-ui*/**");
    }
}
