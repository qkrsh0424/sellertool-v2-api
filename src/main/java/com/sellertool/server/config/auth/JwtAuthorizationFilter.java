package com.sellertool.server.config.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    
    private UserRepository userRepository;
    private TokenUtils tokenUtils;
    private RefreshTokenRepository refreshTokenRepository;
    private String accessTokenSecret;
    private String refreshTokenSecret;

    final static Integer JWT_TOKEN_COOKIE_EXPIRATION = 5*24*60*60; // seconds - 5일

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
        String accessTokenSecret, String refreshTokenSecret) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenUtils = new TokenUtils(accessTokenSecret, refreshTokenSecret);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final Cookie jwtCookie = WebUtils.getCookie(request, "st_actoken");

        if(jwtCookie == null) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = jwtCookie.getValue();

        try{
            Claims claims = Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken).getBody();
            
            // 액세스 토큰이 유효한 경우
            UserEntity userEntity = new UserEntity();
            userEntity.setId(UUID.fromString(claims.get("id").toString()))
                .setEmail(claims.get("email").toString())
                .setRoles(claims.get("roles").toString())
                .setName(claims.get("name").toString());

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            
            // Authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(ExpiredJwtException e) {
            // 액세스 토큰이 만료된 경우
            // 리프레시 토큰을 확인해서 액세스 토큰 발급 여부 결정
            // 리프레시 토큰 조회 - 액세스 클레임에서 refreshTokenId에 대응하는 RefreshToken값 조회
            Optional<RefreshTokenEntity> refreshTokenEntityOpt = refreshTokenRepository.findById(UUID.fromString(e.getClaims().get("refreshTokenId").toString()));
            
            Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(refreshTokenEntityOpt.get().getRefreshToken()).getBody();

            Claims claims = e.getClaims();
            
            System.out.println(claims);
            if(refreshTokenEntityOpt != null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setId(UUID.fromString(claims.get("id").toString()))
                    .setEmail(claims.get("email").toString())
                    .setRoles(claims.get("roles").toString())
                    .setName(claims.get("name").toString());
                
                String newAccessToken = tokenUtils.getJwtAccessToken(userEntity, refreshTokenEntityOpt.get().getId());
                String newRefreshToken = tokenUtils.getJwtRefreshToken();

                refreshTokenRepository.findById(refreshTokenEntityOpt.get().getId()).ifPresent(r -> {
                    r.setRefreshToken(newRefreshToken);
                    refreshTokenRepository.save(r);
                });
                System.out.println("hi");

                ResponseCookie tokenCookie = ResponseCookie.from("st_actoken", newAccessToken)
                        .httpOnly(true)
                        // secure true설정을 하면 https가 아닌 통신에는 쿠키를 전송하지 않음
                        // .secure(true)
                        // 모든 경로에 쿠키를 전송 (path값을 /user로 지정하면 /user와 /user의 하위경로로만 쿠키를 전송)
                        .path("/")
                        .maxAge(JWT_TOKEN_COOKIE_EXPIRATION)
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
                
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                
                // Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch(JwtException e) {
            // 토큰 에러
            System.out.println("2");
        }

        chain.doFilter(request, response);
    }
}
