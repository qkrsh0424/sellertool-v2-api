package com.sellertool.server.config.auth;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.message.model.dto.Message;
import com.sellertool.server.domain.refresh_token.model.repository.RefreshTokenRepository;
import com.sellertool.server.utils.CsrfTokenUtils;
import com.sellertool.server.utils.AuthTokenUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    private String accessTokenSecret;
    private RefreshTokenRepository refreshTokenRepository;

    public JwtLogoutSuccessHandler(String accessTokenSecret, RefreshTokenRepository refreshTokenRepository) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

            Cookie accessCookie = null;
            Cookie csrfCookie = null;
            UUID refreshTokenId = null;

            // 액세스 토큰을 참고해 RefreshToken의 id값을 가져온다
            try{
                accessCookie = WebUtils.getCookie(request, "st_actoken");

                if (!AuthTokenUtils.isValidToken(accessCookie)) {
                    throw new AccessDeniedPermissionException();
                }

                Claims claims = Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessCookie.getValue()).getBody();
                refreshTokenId = UUID.fromString(claims.get("refreshTokenId").toString());
            } catch(ExpiredJwtException e) {    // 토큰 만료
                refreshTokenId = UUID.fromString(e.getClaims().get("refreshTokenId").toString());
            } catch(Exception e) {
                // TODO :: 에러 메세지 제거
                log.error("Access token caused an error.");
            }
            
            // CSRF 토큰 확인
            try{
                csrfCookie = WebUtils.getCookie(request, "csrf_token");
 
                if(!CsrfTokenUtils.isValidToken(csrfCookie)) {
                    throw new AccessDeniedPermissionException();
                }
            } catch (Exception e) { // 토큰 만료 및 에러
                // TODO :: 에러 메세지 제거
                log.error("Csrf token caused an error.");
            }

            // 액세스 쿠키, csrf 쿠키 삭제
            ResponseCookie newAccessCookie = ResponseCookie.from("st_actoken", null)
                    .path("/")
                    .maxAge(0)
                    .build();

            ResponseCookie newCsrfCookie = ResponseCookie.from("csrf_token", null)
                    .path("/")
                    .maxAge(0)
                    .build();

            Message message = new Message();
            message.setMessage("success");
            message.setStatus(HttpStatus.OK);
            message.setMemo("logout success");

            try {
                // 리프레시 토큰 DB 삭제
                refreshTokenRepository.findById(refreshTokenId).ifPresent(item -> {
                    refreshTokenRepository.delete(item);
                });
            } catch(Exception e) {
                message.setMessage("error");
                message.setStatus(HttpStatus.BAD_REQUEST);
            }

            String oms = new ObjectMapper().writeValueAsString(message);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, newCsrfCookie.toString());
            response.getWriter().write(oms);
            response.getWriter().flush();
            return;
    }
}
