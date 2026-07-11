package com.cohabit.cohabitbackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

/**
 * Creates and validates JWT access tokens for authenticated Cohabit users.
 */
@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMillis;

    /**
     * Creates a JWT service using application-provided signing settings.
     *
     * @param secret symmetric signing secret
     * @param expirationMillis access token lifetime in milliseconds
     */
    public JwtService(
            @Value("${cohabit.jwt.secret:change-me-change-me-change-me-change-me-change-me-change-me}") String secret,
            @Value("${cohabit.jwt.expiration-millis:86400000}") long expirationMillis
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    /**
     * Generates a signed JWT for a user.
     *
     * @param userDetails authenticated user details
     * @return signed access token
     */
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMillis)))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extracts the user email stored as the token subject.
     *
     * @param token JWT token
     * @return user email
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates a token against user details and expiration.
     *
     * @param token JWT token
     * @param userDetails expected user details
     * @return true when the token belongs to the user and is not expired
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
