package com.sellertool.server.config.referer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sellertool.server.domain.message.model.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 수정
 * 1. 리퍼러 체크는 Get 요청은 넘긴다.
 */
public class RefererAuthenticationFilter extends BasicAuthenticationFilter {
    final static List<String> refererWhiteList = Arrays.asList(
            "http://localhost:3000",
            "https://www.sellertl.com"
    );

    public RefererAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String referer = request.getHeader("Referer") != null ? request.getHeader("Referer") : null;
            // TODO::https만 받도록 설정해야 함. match.group받아오는 것도 변경
            // String regex = "https:\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?$";
//            String regex = "(http|https):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?(\\/)|((\\/[^\\s/\\/]+)*)?$";

            /**
             * http://www.example.com/api => http://www.example.com
             * http://google.com:8081/api => http://google.com:8081
             * http://www.google.com/api => http://www.google.com
             * http://www.www.google.com/api => http://www.www.google.com
             * http://www.google.com?/api => http://www.google.com
             * http://localhost:8081/api => http://localhost:8081
             * https://localhost.com/api/ => https://localhost.com
             * http://localhost/api/ => http://localhost
             * http://www.localhost/api => http://www.localhost
             * http://www.localhost.com/api => http://www.localhost.com
             * https://api.sellertool.io/api => https://api.sellertool.io
             * https://www.sellertool.io:80/api => https://www.sellertool.io:80
             * https://www.sellertool.io:443/api => https://www.sellertool.io:443
             * http://localhost:80/api => http://localhost:80
             */
            String regex = "https?:\\/\\/(?:localhost|(?:w{1,3}\\.)?[^\\s.]*(?:\\.[a-z]+))*(?::\\d+)?(?![^<]*(?:<\\/\\w+>|\\/?>))";
            Pattern refererPattern = Pattern.compile(regex);
            Matcher match = refererPattern.matcher(referer);

            if (!match.find()) {
                // 올바른 url 패턴이 아닌경우
                request.setAttribute("exception-type", "REFERER_ERROR");
                this.unsuccessfulRefererFilter(request, response);
                return;
            } else {
                // referer white list에 없는 도메인인 경우
                if (!refererWhiteList.contains(match.group())) {
                    request.setAttribute("exception-type", "REFERER_FORBIDDEN");
                    this.unsuccessfulRefererFilter(request, response);
                    return;
                }
            }
        } catch (NullPointerException e) {
            request.setAttribute("exception-type", "REFERER_NOT_FOUND");
            this.unsuccessfulRefererFilter(request, response);
            return;
        } catch (IllegalStateException e) {
            request.setAttribute("exception-type", "REFERER_NOT_FOUND");
            this.unsuccessfulRefererFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    public static void unsuccessfulRefererFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorType = request.getAttribute("exception-type") != null ? request.getAttribute("exception-type").toString() : null;

        Message message = new Message();
        if (errorType.equals("REFERER_ERROR")) {
            message.setMemo("It is not the right url pattern.");
        } else if (errorType.equals("REFERER_FORBIDDEN")) {
            message.setMemo("This is not an allowed domain.");
        } else if (errorType.equals("REFERER_NOT_FOUND")) {
            message.setMemo("Referer not found.");
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
