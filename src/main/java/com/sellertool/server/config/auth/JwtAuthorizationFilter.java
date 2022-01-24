package com.sellertool.server.config.auth;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sellertool.server.config.csrf.CsrfTokenUtils;
import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.refresh_token.model.entity.RefreshTokenEntity;
import com.sellertool.server.domain.refresh_token.model.repository.RefreshTokenRepository;
import com.sellertool.server.domain.user.model.entity.UserEntity;
import com.sellertool.server.domain.user.model.repository.UserRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    
    private UserRepository userRepository;
    private TokenUtils tokenUtils;
    private RefreshTokenRepository refreshTokenRepository;
    private String accessTokenSecret;
    private String refreshTokenSecret;

    final static Integer JWT_TOKEN_COOKIE_EXPIRATION = 5*24*60*60; // seconds - 5일
    final static Integer CSRF_TOKEN_COOKIE_EXPIRATION = 5*24*60*60; // seconds - 5일

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
        String accessTokenSecret, String refreshTokenSecret) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.tokenUtils = new TokenUtils(accessTokenSecret, refreshTokenSecret);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final Cookie jwtCookie = WebUtils.getCookie(request, "st_actoken");
        String ipAddress = this.getClientIpAddress(request);

        // cookied에 액세스토큰 정보가 없다면 체인을 타게 한다
        if(jwtCookie == null) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = jwtCookie.getValue();

        try{
            // 액세스 토큰이 유효한 경우 claim에 있는 정보를 그대로 저장
            Claims claims = Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken).getBody();

            // 요청된 client ip 주소가 기존의 것과 다르다면
            if (!ipAddress.equals(claims.get("ip"))) {
                throw new AccessDeniedPermissionException("This is not a valid user's IP address.");
            }

            UserEntity userEntity = UserEntity.builder()
                .id(UUID.fromString(claims.get("id").toString()))
                .email(claims.get("email").toString())
                .roles(claims.get("roles").toString())
                .build();

            this.saveAuthenticationToSecurityContextHolder(userEntity);
        } catch(ExpiredJwtException e) {    // 액세스 토큰이 만료된 경우 리프레시 토큰을 확인해 액세스 토큰 발급 여부 결정

            // 요청된 client ip 주소가 기존의 것과 다르다면
            if(!ipAddress.equals(e.getClaims().get("ip"))) {
                throw new AccessDeniedPermissionException("This is not a valid user's IP address.");
            }

            // 리프레시 토큰 조회 - 액세스 클레임에서 refreshTokenId에 대응하는 RefreshToken값 조회
            Optional<RefreshTokenEntity> refreshTokenEntityOpt = refreshTokenRepository.findById(UUID.fromString(e.getClaims().get("refreshTokenId").toString()));
            
            Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(refreshTokenEntityOpt.get().getRefreshToken()).getBody();

            Claims claims = e.getClaims();

            if(refreshTokenEntityOpt != null) {
                UserEntity userEntity = UserEntity.builder()
                        .id(UUID.fromString(claims.get("id").toString()))
                        .email(claims.get("email").toString())
                        .build();

                // 새로운 액세스 토큰과 리프레시 토큰을 발급
                String newAccessToken = tokenUtils.getJwtAccessToken(userEntity, refreshTokenEntityOpt.get().getId(), ipAddress);
                String newRefreshToken = tokenUtils.getJwtRefreshToken(ipAddress);

                // DB에 저장된 리프레시 토큰을 새로운 리프레시 토큰으로 업데이트
                refreshTokenRepository.findById(refreshTokenEntityOpt.get().getId()).ifPresent(r -> {
                    r.setRefreshToken(newRefreshToken);
                    r.setUpdatedAt(new Date(System.currentTimeMillis()));

                    refreshTokenRepository.save(r);
                });

                // 새로운 엑세스 토큰을 쿠키에 저장
                ResponseCookie tokenCookie = ResponseCookie.from("st_actoken", newAccessToken)
                        .httpOnly(true)
                        // .secure(true)
                        .path("/")
                        .maxAge(JWT_TOKEN_COOKIE_EXPIRATION)
                        .build();

                // CSRF 토큰 생성
                String csrfToken = CsrfTokenUtils.getCsrfToken();
                String csrfJwtToken = CsrfTokenUtils.getCsrfJwtToken(csrfToken);

                ResponseCookie csrfCookie = ResponseCookie.from("csrf_token", csrfToken)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(CSRF_TOKEN_COOKIE_EXPIRATION)
                        .build();

                ResponseCookie csrfJwtCookie = ResponseCookie.from("csrf_jwt", csrfJwtToken)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(CSRF_TOKEN_COOKIE_EXPIRATION)
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, csrfCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, csrfJwtCookie.toString());

                this.saveAuthenticationToSecurityContextHolder(userEntity);
            }
        } catch(JwtException e) {   // 토큰 에러
            log.error("JWT Token error.");
        } catch(AccessDeniedPermissionException e) {    // 요청된 client ip 주소가 기존의 것과 다르다면
            log.error("This is not a valid user's IP address.");
        }

        chain.doFilter(request, response);
    }

    private void saveAuthenticationToSecurityContextHolder(UserEntity userEntity) {
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                
        // Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
