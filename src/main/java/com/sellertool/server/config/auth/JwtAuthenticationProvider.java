package com.sellertool.server.config.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final PrincipalDetailsService principalDetailsService;
    private final PasswordEncoder passwordEncoder;

    // 실제 인증에 대한 부분
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)authentication;

        // AuthenticationFilter에서 생성된 토큰으로 아이디와 비밀번호 조회
        String email = token.getName();
        String password = token.getCredentials().toString();
        
        // UserDetailsService를 통해 DB에서 사용자 조회
        PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(email);

        // matches(입력값+salt를 인코딩한 값, DB에 인코딩되어 저장된 비밀번호)이 다르다면
        if(!passwordEncoder.matches(password, principalDetails.getPassword())) {
            throw new BadCredentialsException("PASSWORD_ERROR");
        }
        
        return new UsernamePasswordAuthenticationToken(principalDetails, password, principalDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}
