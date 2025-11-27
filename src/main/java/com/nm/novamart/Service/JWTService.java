package com.nm.novamart.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JWTService {

    private final String secretKey;
    private final int expirationHours;

    public JWTService(
            @Value("${app.jwt.secret}") String secretKey,
            @Value("${app.jwt.expiration-hours}") int expirationHours) {

        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("secretKey cannot be null or empty");
        }

        this.secretKey = secretKey;
        this.expirationHours = expirationHours;


    }


    public String generateToken(String email) {

        Date now = new Date();

        Map<String,Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        try {
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error creating JWT secret key: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT secret key configuration", e);
        }
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {

        if(token == null || userDetails == null){
            return false;
        }

        try {
            String username = extractUserName(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }catch (Exception e){
            throw new UsernameNotFoundException("Invalid JWT token" + e.getMessage());
        }

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        if(token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }

        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
