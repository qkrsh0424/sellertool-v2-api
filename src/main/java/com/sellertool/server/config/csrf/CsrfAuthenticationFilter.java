package com.sellertool.server.config.csrf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.message.model.dto.Message;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsrfAuthenticationFilter extends BasicAuthenticationFilter {

    private String csrfTokenSecret;
    private String csrfJwtSecret;
    private CsrfTokenUtils csrfTokenUtils;

    final static Integer CSRF_TOKEN_COOKIE_EXPIRATION = 5*24*60*60; // seconds - 5일

    public CsrfAuthenticationFilter(AuthenticationManager authenticationManager, String csrfTokenSecret, String csrfJwtSecret) {
        super(authenticationManager);
        this.csrfTokenSecret = csrfTokenSecret;
        this.csrfJwtSecret = csrfJwtSecret;
        this.csrfTokenUtils = new CsrfTokenUtils(csrfTokenSecret, csrfJwtSecret);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // TODO::referer 체크
        final Cookie csrfCookie = WebUtils.getCookie(request, "csrf_token");
        final Cookie csrfJwtCookie = WebUtils.getCookie(request, "csrf_jwt");
        
        if(csrfCookie == null || csrfJwtCookie == null) {
            chain.doFilter(request, response);
            return;
        }

        String csrfToken = csrfCookie.getValue();
        String csrfJwtToken = csrfJwtCookie.getValue();

        try{
            // Claims claims = Jwts.parser().setSigningKey(csrfJwtSecret).parseClaimsJws(csrfJwtToken).getBody();
            Claims claims = Jwts.parser().setSigningKey(csrfJwtSecret).parseClaimsJws(csrfJwtToken).getBody();
            
            if(claims.get("csrf").equals(csrfToken)){
                // 성공.
                // 새로운 CSRF Cookie, CSRF Token값 생성
                String newCsrfToken = CsrfTokenUtils.getCsrfToken();
                String newCsrfJwtToken = CsrfTokenUtils.getCsrfJwtToken(newCsrfToken);

                ResponseCookie newCsrfCookie = ResponseCookie.from("csrf_token", newCsrfToken)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(CSRF_TOKEN_COOKIE_EXPIRATION)
                        .build();

                ResponseCookie newCsrfJwtCookie = ResponseCookie.from("csrf_jwt", newCsrfJwtToken)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(CSRF_TOKEN_COOKIE_EXPIRATION)
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, newCsrfCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, newCsrfJwtCookie.toString());
            } else {
                // 실패.
                throw new AccessDeniedPermissionException("This is not a valid CSRF token.");
            }
        } catch(ExpiredJwtException e) {    // CSRF 토큰값이 만료될 수 없다. accessToken이 재생성될 때 CSRF 토큰도 재생성되므로
            log.error("CSRF Jwt Token error.");
        } catch(JwtException e) {   // 토큰 에러
            log.error("JWT Token error.");
        } catch(AccessDeniedPermissionException e) {    // CSRF 토큰값이 올바르지 않다면
            log.error("This is not a valid CSRF token.");
        }

        chain.doFilter(request, response);
    }
}
