package com.catchsecu.gateway.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    // KMS 사용이 필요함
    private final String secretKey = "testtettesttesttesttesttest";
    private String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());


    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date expiration = Date.from(new Date().toInstant().plus(30, ChronoUnit.MINUTES));

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, encodedKey)
                .compact();

        return token;

    }

    @SuppressWarnings("unchecked")
    public Map<String, String> payloadByValidToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(encodedKey)
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();
        ObjectMapper mapper = new ObjectMapper();
        if (expiration.after(new Date())) {
            String payload = claims.getSubject();
            try {
                return mapper.readValue(payload, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }
    }
}
