package com.sellertool.server.config.cors;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsFilterConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // cookie를 요청에 포함하기 위해 credentials설정.
        // TODO :: credentials설정을 하지 않았을 때와 비교
        // TODO :: setAllowedOrigins는 추후에 추가
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8081"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(Arrays.asList("Authorization"));

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
