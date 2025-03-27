package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final UserRepository userRepository;
    @Value("${jwt.expiryTime}")
    private long expiryTime;

    @Value("${jwt.refresh-token.expiryTime}")
    private long refreshExpiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public JwtServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(UserDetails user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails user) {
        return generateToken(extraClaims, user, expiryTime);
    }

    @Override
    public String generateReFreshToken(UserDetails user) {
        return generateToken(new HashMap<>(), user, refreshExpiration);
    }


    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        // Chuyển đổi danh sách GrantedAuthority thành danh sách chuỗi
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found with email: " + userDetails.getUsername());
        }

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("id", user.get().getId())
                .claim("role", roles)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
