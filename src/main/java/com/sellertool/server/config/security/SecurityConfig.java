package com.sellertool.server.config.security;

import com.sellertool.server.config.auth.JwtAuthorizationExceptionFilter;
import com.sellertool.server.config.auth.JwtAuthorizationFilter;
import com.sellertool.server.config.csrf.CsrfAuthenticationFilter;
import com.sellertool.server.config.csrf.CsrfExceptionFilter;
import com.sellertool.server.config.referer.RefererAuthenticationFilter;
import com.sellertool.server.config.referer.RefererExceptionFilter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${csrf.token.secret}")
    private String csrfTokenSecret;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // 토큰을 활용하면 세션이 필요없으므로 STATELESS로 설정해 세션을 사용하지 않는다

        http.cors();
//        http
//                .headers(headers -> headers
//                        // allow same origin to frame our site to support iframe SockJS
//                        .frameOptions(frameOptions -> frameOptions
//                                .sameOrigin()
//                        )
//                );
        http
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
                .anyRequest().permitAll();
        http
                .addFilterBefore(new RefererAuthenticationFilter(), CsrfFilter.class)
                .addFilterAfter(new CsrfAuthenticationFilter(csrfTokenSecret), RefererAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthorizationFilter(accessTokenSecret), CsrfAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthorizationFilter(accessTokenSecret), CsrfFilter.class)
                .addFilterBefore(new RefererExceptionFilter(), RefererAuthenticationFilter.class)
                .addFilterBefore(new CsrfExceptionFilter(), CsrfAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationExceptionFilter(), JwtAuthorizationFilter.class)
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 setting 시에는 시큐리티를 무시한다.
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
        web.httpFirewall(defaultHttpFirewall());
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
}
