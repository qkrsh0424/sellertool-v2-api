package com.sellertool.server.domain.csrf.service;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.sellertool.server.utils.CsrfTokenUtils;
import com.sellertool.server.utils.JwtExpireTimeInterface;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CsrfTokenService {

    @Value("${csrf.token.secret}")
    private String csrfTokenSecret;
    
    private CsrfTokenUtils csrfTokenUtils;
    
    public void getCsrfToken(HttpServletResponse response) {
        this.csrfTokenUtils = new CsrfTokenUtils(csrfTokenSecret);

        // 토큰 생성 및 쿠키 설정
        String csrfTokenId = UUID.randomUUID().toString();
        String csrfJwtToken = CsrfTokenUtils.getCsrfJwtToken(csrfTokenId);

        ResponseCookie csrfJwtCookie = ResponseCookie.from("csrf_token", csrfJwtToken)
            .httpOnly(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(JwtExpireTimeInterface.CSRF_TOKEN_COOKIE_EXPIRATION)
            .build();

        
        response.addHeader(HttpHeaders.SET_COOKIE, csrfJwtCookie.toString());
        response.setHeader("X-XSRF-TOKEN", csrfTokenId);
    }
}
