package com.sellertool.server.domain.hello.controller;

import com.sellertool.server.domain.message.model.dto.Message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/any/hello")
public class HelloAnyApiV1 {
    @GetMapping("")
    public ResponseEntity<?> hello(){
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData("hello");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
