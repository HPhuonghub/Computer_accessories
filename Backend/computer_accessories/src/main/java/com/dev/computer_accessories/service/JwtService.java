package com.dev.computer_accessories.service;

import org.springframework.security.core.userdetails.UserDetails;


public interface JwtService {
    String generateToken(UserDetails User);

    String generateReFreshToken(UserDetails User);

    String extractUsername(String token);

    boolean isValidToken(String token, UserDetails user);

}
