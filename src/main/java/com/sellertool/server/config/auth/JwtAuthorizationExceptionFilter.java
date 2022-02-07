package com.sellertool.server.config.auth;

import com.sellertool.server.config.exception.AuthorizationAccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("============JwtAuthorizationExceptionFilter============");
        try {
            filterChain.doFilter(request, response);
        } catch (AuthorizationAccessDeniedException e) {
            System.out.println("here expcetion");
        }
    }
}
