package com.sellertool.server.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
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
            ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 setting 시에는 시큐리티를 무시한다.
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/h2-console/**");
        web.httpFirewall(defaultHttpFirewall());
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

}
