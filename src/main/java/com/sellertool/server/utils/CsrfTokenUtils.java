package com.sellertool.server.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class CsrfTokenUtils {

    private static String csrfJwtSecret;

    public CsrfTokenUtils(String csrfJwtSecret) {
        this.csrfJwtSecret = csrfJwtSecret;
    }

    public static String getCsrfJwtToken(String csrfTokenId) {
        JwtBuilder builder = Jwts.builder()
            .setSubject("CSRF_JWT")
            .setHeader(createHeader())
            .setClaims(createClaims(csrfTokenId))
            .setExpiration(createTokenExpiration(ExpireTimeInterface.CSRF_TOKEN_COOKIE_EXPIRATION))
            .signWith(SignatureAlgorithm.HS256, createSigningKey(csrfJwtSecret));

        return builder.compact();
    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    // JWT Palyod
    private static Map<String, Object> createClaims(String csrfTokenId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("csrfId", csrfTokenId);
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
