package com.sellertool.server.domain.hello.controller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.annotation.WorkspacePermission;
import com.sellertool.server.domain.hello.service.HelloBusinessService;
import com.sellertool.server.domain.message.dto.Message;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/any/hello")
@RequiredArgsConstructor
public class HelloAnyApiV1 {
    private final HelloBusinessService helloBusinessService;

    @GetMapping("/{hello}")
    @RequiredLogin
    public ResponseEntity<?> hello(@PathVariable(value = "hello") Object hello){
        Message message = new Message();

//        UUID workspaceId = UUID.fromString("7bd601e0-1592-46c0-ac05-a06d855a68f3");
//
//        helloBusinessService.hello1(workspaceId);
//        message.setStatus(HttpStatus.BAD_REQUEST);
//        message.setMessage("success");
//        message.setData("hello");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
