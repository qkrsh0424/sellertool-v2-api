package com.sellertool.server.config.referer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellertool.server.domain.message.model.dto.Message;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class RefererAuthenticationFilter extends BasicAuthenticationFilter {
    final static List<String> refererWhiteList = Arrays.asList("localhost", "www.sellertl.com");
    
    public RefererAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String referer = request.getHeader("Referer") != null ? request.getHeader("Referer") : null;

            // TODO::https만 받도록 설정해야 함. match.group받아오는 것도 변경
            // String regex = "https:\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?$";
            String regex = "(http|https):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?$";
            Pattern refererPattern = Pattern.compile(regex);
            Matcher match = refererPattern.matcher(referer);

            if (!match.find()) {
                // 올바른 url 패턴이 아닌경우
                request.setAttribute("exception-type", "REFERER_ERROR");
                this.unsuccessfulRefererFilter(request, response);
                return;
            } else {
                // referer white list에 없는 도메인인 경우
                if (!refererWhiteList.contains(match.group(2))) {
                    request.setAttribute("exception-type", "REFERER_FORBIDDEN");
                    this.unsuccessfulRefererFilter(request, response);
                    return;
                }
            }
        } catch (NullPointerException e) {
            request.setAttribute("exception-type", "REFERER_NOT_FOUND");
            this.unsuccessfulRefererFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    public static void unsuccessfulRefererFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorType = request.getAttribute("exception-type") != null ? request.getAttribute("exception-type").toString() : null;

        Message message = new Message();
        if(errorType.equals("REFERER_ERROR")) {
            message.setMemo("It is not the right url pattern.");
        }else if(errorType.equals("REFERER_FORBIDDEN")) {
            message.setMemo("This is not an allowed domain.");
        }else if(errorType.equals("REFERER_NOT_FOUND")) {
            message.setMemo("Referer not found.");
        }else {
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
