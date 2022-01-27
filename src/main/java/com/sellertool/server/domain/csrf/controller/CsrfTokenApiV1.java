package com.sellertool.server.domain.csrf.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sellertool.server.domain.csrf.service.CsrfTokenService;
import com.sellertool.server.domain.message.model.dto.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/csrf")
public class CsrfTokenApiV1 {
    
    @Autowired
    private CsrfTokenService csrfTokenService;

    @GetMapping("")
    public ResponseEntity<?> getCsrfToken(HttpServletRequest request, HttpServletResponse response){
        Message message = new Message();

        csrfTokenService.getCsrfToken(response);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData("csrf");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("")
    public ResponseEntity<?> testCsrfToken(HttpServletRequest request, HttpServletResponse response){
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData("csrf test success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
