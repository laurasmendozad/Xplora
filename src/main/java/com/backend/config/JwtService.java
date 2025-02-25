package com.backend.config;

import com.backend.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    private String generateToken(UserDetails userDetails){
        Usuario usuario = (Usuario) userDetails;
        Map<String, Object>  claims = Map.of(
                "rol", userDetails.getAuthorities(),
                "nombre", usuario.getNombre(),
                "apellido", usuario.getApellido()
        );
        return Jwts.builder()
                .claims(claims) // Agregar claims
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
