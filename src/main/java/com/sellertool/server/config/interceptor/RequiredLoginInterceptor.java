package com.sellertool.server.config.interceptor;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.exception.dto.InvalidUserAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequiredLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod == false) {
            return false;
        }

        HandlerMethod method = (HandlerMethod) handler;

        RequiredLogin requiredLogin = method.getMethodAnnotation(RequiredLogin.class);

        if (requiredLogin == null) {
            return true;
        }

        if (
                SecurityContextHolder.getContext().getAuthentication().getName() != null &&
                        SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")
        ) {
            throw new InvalidUserAuthException("로그인이 필요한 서비스 입니다.");
        } else {
            return true;
        }
    }
}
