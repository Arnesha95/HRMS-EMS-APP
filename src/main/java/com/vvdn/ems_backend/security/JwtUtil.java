package com.vvdn.ems_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final String SECRET = "my-super-secret-key-my-super-secret-key-123456";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("type", "access")
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
//                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
//                .compact();
//    }


    public String generateAccessToken(UUID id, String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", id.toString())
                .claim("role", role)
                .claim("type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }


    public String generateRefreshToken(UUID id, String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", id.toString())
                .claim("role", role)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
               // .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .signWith(key)
                .compact();
    }

    //token type
    public String extractTokenType(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET.getBytes())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("type", String.class);
        return extractAllClaims(token).get("type", String.class);
   }

    public String extractUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET.getBytes())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
        return extractAllClaims(token).getSubject();
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }



    //validate token
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateToken(String token, String expectedType) {
        if (!validateToken(token)) {
            return false;
        }

        String actualType = extractTokenType(token);
        return expectedType.equals(actualType);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

