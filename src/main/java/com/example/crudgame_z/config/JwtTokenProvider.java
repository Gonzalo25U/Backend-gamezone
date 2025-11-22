package com.example.crudgame_z.config;

import com.example.crudgame_z.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ==================================
    // GENERAR TOKEN
    // ==================================
    public String generateToken(UUID userId, String email, Role role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId.toString())   // Cambiado: "id" → "userId"
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ==================================
    // VALIDAR TOKEN
    // ==================================
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (JwtException e) {
            return false;
        }
    }

    // ==================================
    // OBTENER EMAIL
    // ==================================
    public String getEmailFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    // ==================================
    // OBTENER USER ID (UUID)
    // ==================================
    public UUID getUserIdFromToken(String token) {
        String idString = getAllClaims(token).get("userId", String.class);
        return UUID.fromString(idString);
    }

    // ==================================
    // OBTENER ROL (opcional)
    // ==================================
    public String getRoleFromToken(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    // ==================================
    // OBTENER TODOS LOS CLAIMS
    // ==================================
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
