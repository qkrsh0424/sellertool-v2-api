package com.sellertool.server.config.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellertool.server.domain.message.dto.Message;
import com.sellertool.server.domain.user.model.entity.UserEntity;
import com.sellertool.server.domain.user.repository.UserRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private UserRepository userRepository;
    private TokenUtils tokenUtils;

    final static Integer JWT_TOKEN_COOKIE_EXPIRATION = 5*24*60*60; // seconds - 5일

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, String accessTokenSecret, String refreshTokenSecret) {
        super.setAuthenticationManager(authenticationManager);
        this.userRepository = userRepository;
        this.tokenUtils = new TokenUtils(accessTokenSecret, refreshTokenSecret);

        this.setFilterProcessesUrl("/api/v1/user/login");
    }

    // 로그인 요청 시 실행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken authToken;

        // POST 메소드가 아니면 에러처리
        if(!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try{
            // request에서 읽어온 값을 json 파싱
            UserEntity user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);

            Optional<UserEntity> userOpt = userRepository.findByEmail(user.getEmail());
            if(!userOpt.isPresent()) {
                request.setAttribute("exception-type", "LOGIN_ERROR");

                // security 로그인 시 아이디와 비밀번호 오류 시 발생하는 오류
                throw new BadCredentialsException("email not found.");
            }
            
            String fullPassword = user.getPassword() + userOpt.get().getSalt();
            
            authToken = new UsernamePasswordAuthenticationToken(user.getEmail(), fullPassword);
        } catch (IOException e) {
            request.setAttribute("exception-type", "INPUT_ERROR");
            throw new AuthenticationServiceException("input error.");
        }

        this.setDetails(request, authToken);
        // AuthenticationFilter는 생성한 UsernamePasswordToken을 AuthenticationManager에게 전달
        return this.getAuthenticationManager().authenticate(authToken);
    }

    // 로그인 성공 시
    @Override
    protected void successfulAuthentication(HttpServletRequest requset, HttpServletResponse response,
     FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UserEntity user = ((PrincipalDetails)authentication.getPrincipal()).getUser();
        String accessToken = TokenUtils.getJwtAccessToken(user);
        String refreshToken = TokenUtils.getJwtRefreshToken();


        ResponseCookie tokenCookie = ResponseCookie.from("st_token", accessToken)
            .httpOnly(true)
            // secure true설정을 하면 https가 아닌 통신에는 쿠키를 전송하지 않음
            // .secure(true)
            // 모든 경로에 쿠키를 전송 (path값을 /user로 지정하면 /user와 /user의 하위경로로만 쿠키를 전송)
            .path("/")
            .maxAge(JWT_TOKEN_COOKIE_EXPIRATION)
            .build();
        
        Message message = new Message();
        message.setMessage("success");
        message.setStatus(HttpStatus.OK);

        String msg = new ObjectMapper().writeValueAsString(message);
        
        response.setStatus(HttpStatus.OK.value());
        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(msg);
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        String errorType = request.getAttribute("exception-type").toString();

        Message message = new Message();

        if(errorType.equals("LOGIN_ERROR")) {
            message.setMessage(errorType);
            message.setStatus(HttpStatus.UNAUTHORIZED);
            message.setMemo("email not exist or password not matched.");
        } else if(errorType.equals("INPUT_ERROR")) {
            message.setMessage(errorType);
            message.setStatus(HttpStatus.UNAUTHORIZED);
            message.setMemo("input error.");
        }

        String msg = new ObjectMapper().writeValueAsString(message);
        
        response.setStatus(message.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(msg);
        response.getWriter().flush();
    }
    
}
