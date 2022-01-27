package com.sellertool.server.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.xml.bind.DatatypeConverter;

import com.sellertool.server.domain.user.model.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtils {
    private static String accessTokenSecret;
    private static String refreshTokenSecret;

    public TokenUtils(String accessTokenSecret, String refreshTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
    }

    public static String getJwtAccessToken(UserEntity userEntity, UUID refreshTokenId, String ipAddress) {
        JwtBuilder builder = Jwts.builder()
            .setSubject(userEntity.getEmail() + "JWT_ACT")
            .setHeader(createHeader())
            .setClaims(createClaims(userEntity, refreshTokenId, ipAddress))
            .setExpiration(createTokenExpiration(JwtExpireTimeInterface.JWT_TOKEN_EXPIRATION))
            .signWith(SignatureAlgorithm.HS256, createSigningKey(accessTokenSecret));

        return builder.compact();
    }

    public static String getJwtRefreshToken(String ipAddress) {
        JwtBuilder builder = Jwts.builder()
            .setSubject("JWT_RFT")
            .setHeader(createHeader())
            .setClaims(createRefreshTokenClaims(ipAddress))
            .setExpiration(createTokenExpiration(JwtExpireTimeInterface.REFRESH_TOKEN_JWT_EXPIRATION))
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
    private static Map<String, Object> createClaims(UserEntity userEntity, UUID refreshTokenId, String ipAddress) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userEntity.getId());
        claims.put("email", userEntity.getEmail());
        claims.put("roles", userEntity.getRoles());
        claims.put("ip", ipAddress);
        claims.put("refreshTokenId", refreshTokenId);
        return claims;
    }

    private static Map<String, Object> createRefreshTokenClaims(String ipAddress) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ip", ipAddress);
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

    public static boolean isValidToken(Cookie jwtCookie) {

        String accessToken = jwtCookie.getValue();

        try {
            Claims claims = Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken).getBody();
            log.info("expireTime :" + claims.getExpiration());
            log.info("email :" + claims.get("email"));
            log.info("roles :" + claims.get("roles"));
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        }
    }
}
