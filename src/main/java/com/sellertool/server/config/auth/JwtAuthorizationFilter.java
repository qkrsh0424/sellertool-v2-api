package com.sellertool.server.config.auth;

import com.sellertool.server.domain.user.entity.UserEntity;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private String accessTokenSecret;


    /**
     * wild card => **
     * example => /wsc/**
     */
    final static List<String> excludeUrls = Arrays.asList(
    );

    public JwtAuthorizationFilter(
            String accessTokenSecret
    ) {
        this.accessTokenSecret = accessTokenSecret;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        AntPathMatcher pathMatcher = new AntPathMatcher();

//        CSRF 발급 등 excludeUrls 에 등록된 url 은 필터를 타지 않게 한다.
        if(excludeUrls.stream().anyMatch(r->pathMatcher.match(r, path))){
            filterChain.doFilter(request, response);
            return;
        }

        Cookie jwtCookie = WebUtils.getCookie(request, "st_actoken");
        String[] ipAddress = this.getClientIpAddress(request).replaceAll(" ", "").split(",");
        String clientIp = ipAddress[0];

        if (jwtCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtCookie.getValue();

        Claims claims = null;

        try {
//            액세스 토큰 정보가 유효 하다면 컨텍스트 홀더에 저장 후 필터를 계속 타게 한다.
            claims = Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken).getBody();
            UUID id = UUID.fromString(claims.get("id").toString());
            String roles = claims.get("roles").toString();

            if (claims == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!clientIp.equals(claims.get("ip"))) {
                filterChain.doFilter(request, response);
                return;
            }

            UserEntity userEntity = UserEntity.builder()
                    .id(id)
                    .roles(roles)
                    .build();

            this.saveAuthenticationToSecurityContextHolder(userEntity);

            filterChain.doFilter(request, response);
            return;
        } catch (ExpiredJwtException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (UnsupportedJwtException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (MalformedJwtException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (SignatureException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (IllegalArgumentException e) {
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
    }


    private void saveAuthenticationToSecurityContextHolder(UserEntity userEntity) {
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);


        // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(principalDetails, null,
                principalDetails.getAuthorities());



        // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
        SecurityContextHolder.getContext().setAuthentication(userAuth);
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
