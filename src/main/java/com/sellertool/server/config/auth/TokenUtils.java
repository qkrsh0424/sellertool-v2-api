package com.sellertool.server.config.auth;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.sellertool.server.domain.user.model.entity.UserEntity;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtils {
    private static String accessTokenSecret;
    private static String refreshTokenSecret;

    public TokenUtils(String accessTokenSecret, String refreshTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
    }

    // final static Integer JWT_TOKEN_EXPIRATION = 1*1000;  // milliseconds - 1분
    final static Integer JWT_TOKEN_EXPIRATION = 20*60*1000;  // milliseconds - 20분
    final static Integer REFRESH_TOKEN_JWT_EXPIRATION = 5*24*60*60*1000;   // milliseconds - 5일

    public static String getJwtAccessToken(UserEntity userEntity, UUID refreshTokenId) {
        JwtBuilder builder = Jwts.builder()
            .setSubject(userEntity.getEmail() + "JWT_ACT")
            .setHeader(createHeader())
            .setClaims(createClaims(userEntity, refreshTokenId))
            .setExpiration(createExpiration())
            .signWith(SignatureAlgorithm.HS256, createSigningKey(accessTokenSecret));

        return builder.compact();
    }

    public static String getJwtRefreshToken() {
        JwtBuilder builder = Jwts.builder()
            .setSubject("JWT_RFT")
            .setHeader(createHeader())
            .setExpiration(createRefreshTokenExpiration())
            .signWith(SignatureAlgorithm.HS256, createSigningKey(refreshTokenSecret));
        
        return builder.compact();
    }
    
    // JWT Header
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    // JWT Palyod
    private static Map<String, Object> createClaims(UserEntity userEntity, UUID refreshTokenId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userEntity.getId());
        claims.put("email", userEntity.getEmail());
        claims.put("roles", userEntity.getRoles());
        claims.put("name", userEntity.getName());
        claims.put("refreshTokenId", refreshTokenId);
        return claims;
    }

    private static Date createExpiration() {
        Date expiration = new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION);
        return expiration;
    }

    private static Date createRefreshTokenExpiration() {
        Date expiration = new Date(System.currentTimeMillis() + REFRESH_TOKEN_JWT_EXPIRATION);
        return expiration;
    }

    private static Key createSigningKey(String tokenSecret) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(tokenSecret);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName()); 
    }
}