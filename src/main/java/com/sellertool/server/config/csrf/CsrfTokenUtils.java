package com.sellertool.server.config.csrf;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class CsrfTokenUtils {
    private static String csrfTokenSecret;
    private static String csrfJwtSecret;

    public CsrfTokenUtils(String csrfTokenSecret, String csrfJwtSecret) {
        this.csrfTokenSecret = csrfTokenSecret;
        this.csrfJwtSecret = csrfJwtSecret;
    }

    final static Integer CSRF_TOKEN_COOKIE_EXPIRATION = 5*24*60*60; // seconds - 5일
    final static Integer CSRF_JWT_COOKIE_EXPIRATION = 5*24*60*60; // seconds - 5일

    public static String getCsrfToken() {
        return UUID.randomUUID().toString();
    }

    public static String getCsrfJwtToken(String csrfToken) {
        JwtBuilder builder = Jwts.builder()
            .setSubject("CSRF_JWT")
            .setHeader(createHeader())
            .setClaims(createClaims(csrfToken))
            .setExpiration(createTokenExpiration(CSRF_JWT_COOKIE_EXPIRATION))
            .signWith(SignatureAlgorithm.HS256, createSigningKey(csrfJwtSecret));

        return builder.compact();
    }

    // CSRR Header
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    // JWT Palyod
    private static Map<String, Object> createClaims(String csrfToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("csrf", csrfToken);
        return claims;
    }

    private static Date createTokenExpiration(Integer expirationTime) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        return expiration;
    }

    private static Key createSigningKey(String tokenSecret) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenSecret);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName()); 
    }

}
