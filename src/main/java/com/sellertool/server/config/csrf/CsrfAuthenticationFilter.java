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
import com.sellertool.server.utils.CsrfTokenUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private CsrfTokenUtils csrfTokenUtils;

    public CsrfAuthenticationFilter(AuthenticationManager authenticationManager, String csrfTokenSecret) {
        super(authenticationManager);
        this.csrfTokenSecret = csrfTokenSecret;
//        생성자로 초기화 시키면 안됨. 스태틱 메서드를 사용할때 csrfTokenSecret을 파라메터로 넣어서 메서드를 불러와야됨.
        this.csrfTokenUtils = new CsrfTokenUtils(csrfTokenSecret);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        
        // GET 메소드는 CsrfFilter를 타지 않는다
        if(request.getMethod().equals("GET")){
            chain.doFilter(request, response);
            return;
        }
            
        try{
            Cookie csrfCookie = WebUtils.getCookie(request, "csrf_token");
            String csrfToken = csrfCookie.getValue();

            Claims claims = Jwts.parser().setSigningKey(csrfTokenSecret).parseClaimsJws(csrfToken).getBody();
            // Cookie값과 csrf설정 헤더값이 동일하지 않다면
            if(!claims.get("csrfId").equals(request.getHeader("X-XSRF-TOKEN"))){
                throw new AccessDeniedPermissionException();
            }else{
                chain.doFilter(request, response);
                return;
            }
        }catch(ExpiredJwtException e) {     // 토큰 만료
            request.setAttribute("exception-type", "CSRF_TOKEN_EXPIRED");
        } catch(JwtException e) {   // 토큰 에러
            request.setAttribute("exception-type", "CSRF_TOKEN_ERROR");
        } catch(AccessDeniedPermissionException e) {    // CSRF 토큰값이 다르다면
            request.setAttribute("exception-type", "CSRF_ACCESS_DENIED");
        } catch(NullPointerException e) {   // CSRF 쿠키가 존재하지 않는다면
            request.setAttribute("exception-type", "CSRF_COOKIE_ERROR");
        }
        this.unsuccessfulCsrfFilter(request, response);
    }

    public static void unsuccessfulCsrfFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorType = request.getAttribute("exception-type") != null ? request.getAttribute("exception-type").toString() : null;

        Message message = new Message();
        if(errorType.equals("CSRF_TOKEN_EXPIRED")) {
            message.setMemo("CSRF Jwt Token error.");
        } else if(errorType.equals("CSRF_TOKEN_ERROR")) {
            message.setMemo("JWT Token error.");
        } else if(errorType.equals("CSRF_ACCESS_DENIED")) {
            message.setMemo("This is not a valid CSRF token.");
        } else if(errorType.equals("CSRF_COOKIE_ERROR")) {
            message.setMemo("CSRF cookie does not exist.");
        } else {
            message.setMemo("undefined error.");
        }
        message.setMessage(errorType);
        message.setStatus(HttpStatus.FORBIDDEN);

        String msg = new ObjectMapper().writeValueAsString(message);
        response.setStatus(message.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(msg);
        response.getWriter().flush();
    }
}
