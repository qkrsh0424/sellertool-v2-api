package com.sellertool.server.domain.logout.controller;

import com.sellertool.server.domain.message.model.dto.Message;
import com.sellertool.server.utils.CustomCookieInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/logout")
public class LogoutApiV1 {
    @PostMapping("")
    public ResponseEntity<?> logout(HttpServletResponse response){
        Message message = new Message();

        ResponseCookie accessToken = ResponseCookie.from("st_actoken", null)
                .path("/")
                .httpOnly(true)
                .sameSite("Strict")
                .domain(CustomCookieInterface.COOKIE_DOMAIN)
                .maxAge(0)
                .secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setMemo("logout");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
