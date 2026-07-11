package com.dcm.backend.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service responsible for generating and validating JWT tokens.
 */
@Service
public class JwtService {

    private final Key signingKey;
    private final long jwtExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long jwtExpiration) {

        this.jwtExpiration = jwtExpiration;

        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
    }

    /**
     * Generates JWT token.
     */
    public String generateToken(CustomUserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("employeeId", userDetails.getEmployeeId());
        claims.put("nickname", userDetails.getNickname());
        claims.put("role", userDetails.getRole());

        return buildToken(claims, userDetails);
    }

    /**
     * Builds JWT.
     */
    private String buildToken(
            Map<String, Object> claims,
            CustomUserDetails userDetails) {

        Date now = new Date();

        Date expiry = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extract username from token.
     */
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract employeeId.
     */
    public String extractEmployeeId(String token) {

        return extractAllClaims(token)
                .get("employeeId", String.class);
    }

    /**
     * Extract nickname.
     */
    public String extractNickname(String token) {

        return extractAllClaims(token)
                .get("nickname", String.class);
    }

    /**
     * Extract role.
     */
    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    /**
     * Generic claim extractor.
     */
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    /**
     * Validates token.
     */
    public boolean isTokenValid(
            String token,
            CustomUserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Checks expiration.
     */
    public boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    /**
     * Extract expiration.
     */
    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract all claims.
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}