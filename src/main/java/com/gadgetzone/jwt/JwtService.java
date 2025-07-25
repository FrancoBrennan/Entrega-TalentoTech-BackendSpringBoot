package com.gadgetzone.jwt;

import com.gadgetzone.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;


import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String generateToken(UserDetails userDetails) {
        Usuario usuario = (Usuario) userDetails;
        return Jwts.builder()
                .setSubject(String.valueOf(usuario.getId())) // Usamos el ID como subject
                .claim("authorities", usuario.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).getSubject());
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        Usuario usuario = (Usuario) userDetails;
        return extractUserId(token).equals(usuario.getId()) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
