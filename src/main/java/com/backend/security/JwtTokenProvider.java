package com.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = "secret"; // Debe ser un valor seguro en producción
    private static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    // Método para generar un token JWT
    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Método para validar el token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Método para obtener la autenticación a partir del token
    public Authentication getAuthentication(String token) {
        String username = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        var userDetails = new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>());
        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }
}