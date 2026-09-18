package com.eu.matrimonybackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret:9a2f8c2e4b1a6d8e0f3c5b7a9e1d3f5a7c9b1e3f5a7c9b1e3f5a7c9b1e3f5a7c}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds:86400000}")
    private long jwtExpirationDate;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Issues a signed JWT for an authenticated user, embedding their granted authorities
     * as a {@code roles} claim so clients can read role membership without a separate call.
     *
     * @param authentication the successfully authenticated principal, whose name (email) becomes
     *                        the token subject and whose authorities are copied into the roles claim
     * @return a compact, HMAC-signed JWT string valid for {@code app.jwt-expiration-milliseconds}
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * Verifies a JWT's signature and expiry against this service's secret key.
     *
     * @param token the raw compact JWT string extracted from the Authorization header
     * @return {@code true} if the token is well-formed, signed with the expected key, and not
     *         expired; {@code false} for any parsing, signature, or expiry failure
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key()).build().parse(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}