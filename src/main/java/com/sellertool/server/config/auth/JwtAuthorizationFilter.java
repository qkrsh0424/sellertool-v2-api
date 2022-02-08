package com.sellertool.server.config.auth;

import com.sellertool.server.domain.refresh_token.model.entity.RefreshTokenEntity;
import com.sellertool.server.domain.refresh_token.model.repository.RefreshTokenRepository;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.user.repository.UserRepository;
import com.sellertool.server.utils.AuthTokenUtils;
import com.sellertool.server.utils.CustomCookieInterface;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private UserRepository userRepository;
    private AuthTokenUtils tokenUtils;
    private RefreshTokenRepository refreshTokenRepository;
    private String accessTokenSecret;
    private String refreshTokenSecret;

//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository,
//                                  String accessTokenSecret, String refreshTokenSecret) {
//        super(authenticationManager);
//        this.refreshTokenRepository = refreshTokenRepository;
//        this.accessTokenSecret = accessTokenSecret;
//        this.refreshTokenSecret = refreshTokenSecret;
//        this.tokenUtils = new TokenUtils(accessTokenSecret, refreshTokenSecret);
//    }

    public JwtAuthorizationFilter(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            String accessTokenSecret,
            String refreshTokenSecret
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.tokenUtils = new AuthTokenUtils(accessTokenSecret, refreshTokenSecret);
    }
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        System.out.println("================ JwtAuthoriztion =================");
//        final Cookie jwtCookie = WebUtils.getCookie(request, "st_actoken");
//        String[] ipAddress = this.getClientIpAddress(request).replaceAll(" ","").split(",");
//        String clientIp = ipAddress[0];
//
//        // cookie에 액세스토큰 정보가 없다면 체인을 타게 한다
//        if(jwtCookie == null) {
//            chain.doFilter(request, response);
//            return;
//        }
//        System.out.println("================ Cookie exist =================");
//        String accessToken = jwtCookie.getValue();
//
//        try{
//            // 액세스 토큰이 유효한 경우 claim에 있는 정보를 그대로 저장
//            Claims claims = Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken).getBody();
//
//            // IP Check. 요청된 client ip 주소가 기존의 것과 다르다면
//            if (!clientIp.equals(claims.get("ip"))) {
//                throw new AccessDeniedPermissionException("This is not a valid user's IP address.");
//            }
//
//            UserEntity userEntity = UserEntity.builder()
//                .id(UUID.fromString(claims.get("id").toString()))
//                .roles(claims.get("roles").toString())
//                .build();
//
//            this.saveAuthenticationToSecurityContextHolder(userEntity);
//        } catch(ExpiredJwtException e) {    // 액세스 토큰이 만료된 경우
//            // 요청된 client ip 주소가 기존의 것과 다르다면
//            if(!clientIp.equals(e.getClaims().get("ip"))) {
//                throw new AccessDeniedPermissionException("This is not a valid user's IP address.");
//            }
//
//            // 리프레시 토큰을 조회해 액세스 토큰 발급 여부 결정 - 액세스 클레임에서 refreshTokenId에 대응하는 RefreshToken값 조회
//            Optional<RefreshTokenEntity> refreshTokenEntityOpt = refreshTokenRepository.findById(UUID.fromString(e.getClaims().get("refreshTokenId").toString()));
//
//            Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(refreshTokenEntityOpt.get().getRefreshToken()).getBody();
//            Claims claims = e.getClaims();
//
//            if(refreshTokenEntityOpt != null) {
//                UserEntity userEntity = UserEntity.builder()
//                        .id(UUID.fromString(claims.get("id").toString()))
//                        .build();
//
//                // 새로운 액세스 토큰과 리프레시 토큰을 발급
//                String newAccessToken = tokenUtils.getJwtAccessToken(userEntity, refreshTokenEntityOpt.get().getId(), clientIp);
//                String newRefreshToken = tokenUtils.getJwtRefreshToken(clientIp);
//
//                // DB에 저장된 리프레시 토큰을 새로운 리프레시 토큰으로 업데이트
//                refreshTokenRepository.findById(refreshTokenEntityOpt.get().getId()).ifPresent(r -> {
//                    r.setRefreshToken(newRefreshToken);
//                    r.setUpdatedAt(new Date(System.currentTimeMillis()));
//
//                    refreshTokenRepository.save(r);
//                });
//
//                // 새로운 엑세스 토큰을 쿠키에 저장
//                ResponseCookie tokenCookie = ResponseCookie.from("st_actoken", newAccessToken)
//                        .httpOnly(true)
//                        // .secure(true)
//                        .sameSite("Strict")
//                        .path("/")
//                        .maxAge(ExpireTimeInterface.JWT_TOKEN_COOKIE_EXPIRATION)
//                        .build();
//
//                response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
//                this.saveAuthenticationToSecurityContextHolder(userEntity);
//            }
//        } catch(JwtException e) {   // 토큰 에러
//            log.error("JWT Token error.");
//        } catch(AccessDeniedPermissionException e) {    // 요청된 client ip 주소가 기존의 것과 다르다면
//            log.error("This is not a valid user's IP address.");
//        }
//        chain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("============JwtAuthorizationFilter============");
        Cookie jwtCookie = WebUtils.getCookie(request, "st_actoken");
        String[] ipAddress = this.getClientIpAddress(request).replaceAll(" ", "").split(",");
        String clientIp = ipAddress[0];

        // cookie에 액세스토큰 정보가 없다면 체인을 타게 한다
        if (jwtCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = jwtCookie.getValue();

        Claims claims = null;
        boolean accessTokenExpired = false;

        try {
            claims = Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
            accessTokenExpired = true;
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

        if (claims == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!clientIp.equals(claims.get("ip"))) {
            filterChain.doFilter(request, response);
            return;
        }

        if(accessTokenExpired == true){
            // 리프레시 토큰을 조회해 액세스 토큰 발급 여부 결정 - 액세스 클레임에서 refreshTokenId에 대응하는 RefreshToken값 조회
            Optional<RefreshTokenEntity> refreshTokenEntityOpt = refreshTokenRepository.findById(UUID.fromString(claims.get("refreshTokenId").toString()));

            if (refreshTokenEntityOpt.isPresent()) {
                RefreshTokenEntity refreshTokenEntity = refreshTokenEntityOpt.get();
                Claims refreshTokenClaims = null;

                try {
                    refreshTokenClaims = Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(refreshTokenEntityOpt.get().getRefreshToken()).getBody();
                } catch (Exception e) {
                    filterChain.doFilter(request, response);
                    return;
                }

                UUID id = UUID.fromString(refreshTokenClaims.get("id").toString());
                String roles = refreshTokenClaims.get("roles").toString();

                // 새로운 액세스 토큰과 리프레시 토큰을 발급
                String newAccessToken = tokenUtils.getJwtAccessToken(id, roles, refreshTokenEntityOpt.get().getId(), clientIp);
                String newRefreshToken = tokenUtils.getJwtRefreshToken(id, roles, clientIp);

                // DB에 저장된 리프레시 토큰을 새로운 리프레시 토큰으로 업데이트
                refreshTokenEntity.setRefreshToken(newRefreshToken);
                refreshTokenEntity.setUpdatedAt(new Date(System.currentTimeMillis()));
                refreshTokenRepository.save(refreshTokenEntity);

                // 새로운 엑세스 토큰을 쿠키에 저장
                ResponseCookie tokenCookie = ResponseCookie.from("st_actoken", newAccessToken)
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("Strict")
                        .domain(CustomCookieInterface.COOKIE_DOMAIN)
                        .path("/")
                        .maxAge(CustomCookieInterface.JWT_TOKEN_COOKIE_EXPIRATION)
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());

                UserEntity userEntity = UserEntity.builder()
                        .id(id)
                        .roles(roles)
                        .build();
                this.saveAuthenticationToSecurityContextHolder(userEntity);
            }

            filterChain.doFilter(request, response);
            return;
        }

        UUID id = UUID.fromString(claims.get("id").toString());
        String roles = claims.get("roles").toString();

        UserEntity userEntity = UserEntity.builder()
                .id(id)
                .roles(roles)
                .build();
        this.saveAuthenticationToSecurityContextHolder(userEntity);

        filterChain.doFilter(request, response);
    }

    private void saveAuthenticationToSecurityContextHolder(UserEntity userEntity) {
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                principalDetails.getAuthorities());

        // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
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
