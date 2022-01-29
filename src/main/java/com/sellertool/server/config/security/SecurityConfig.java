package com.sellertool.server.config.security;

import com.sellertool.server.config.auth.JwtAuthenticationFilter;
import com.sellertool.server.config.auth.JwtAuthenticationProvider;
import com.sellertool.server.config.auth.JwtAuthorizationFilter;
import com.sellertool.server.config.auth.PrincipalDetailsService;
import com.sellertool.server.config.csrf.CsrfAuthenticationFilter;
import com.sellertool.server.config.referer.RefererAuthenticationFilter;
import com.sellertool.server.domain.refresh_token.model.repository.RefreshTokenRepository;
import com.sellertool.server.domain.user.model.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PrincipalDetailsService principalDetailsService;

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh.secret")
    private String refreshTokenSecret;

    @Value("${csrf.token.secret}")
    private String csrfTokenSecret;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 토큰을 활용하면 세션이 필요없으므로 STATELESS로 설정해 세션을 사용하지 않는다
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .cors()
            .and()
                .authorizeRequests()
                .antMatchers("/api/v1/superadmin/**")
                .access("hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/api/v1/membership/**")
                .access("hasRole('ROLE_MEMBERSHIP') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/api/v1/member/**")
                .access("hasRole('ROLE_MEMBER') or hasRole('ROLE_MEMBERSHIP') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
                .antMatchers("/api/v1/any/**")
                .permitAll()
                .anyRequest().permitAll()
            .and()
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .addFilterBefore(new CsrfAuthenticationFilter(authenticationManager(), csrfTokenSecret), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RefererAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), userRepository, refreshTokenRepository, accessTokenSecret, refreshTokenSecret), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), refreshTokenRepository, accessTokenSecret, refreshTokenSecret), UsernamePasswordAuthenticationFilter.class)
            ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 setting 시에는 시큐리티를 무시한다.
    public void configure(WebSecurity web)throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
        web.httpFirewall(defaultHttpFirewall());
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(principalDetailsService, passwordEncoder());
    }
}
