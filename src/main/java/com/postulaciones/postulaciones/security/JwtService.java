package com.postulaciones.postulaciones.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generarToken(String correo) {
        return Jwts.builder()
                .subject(correo) // Se guarda el correo para que cuando haga la peticion saber que usuario la
                                 // realizo
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey()) // Firma para uqe nadie lo modifique
                .compact(); // Genera el String
    }

    // EN ADELANTE PARA VALIDAR TOKEN
    public String extraerCorreo(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // UserDetails es la representación de un usuario que Spring Security entiende.
    public boolean esTokenValido(String token, UserDetails userDetails) {

        String correo = extraerCorreo(token);

        return correo.equals(userDetails.getUsername())
                && !estaExpirado(token);
    }

    private boolean estaExpirado(String token) {
        return extraerFechaExpiracion(token).before(new Date());
    }

    private Date extraerFechaExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    private <T> T extraerClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
