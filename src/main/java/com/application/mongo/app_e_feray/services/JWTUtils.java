package com.application.mongo.app_e_feray.services;

import java.security.AlgorithmConstraints;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtils {

    @Value("${app.secret-key}")
    private String secreykey;

    // private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSecretkey())
                .compact();
    }

    private Key getSecretkey() {
        return new SecretKeySpec(secreykey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(getSecretkey()).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSecretkey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
