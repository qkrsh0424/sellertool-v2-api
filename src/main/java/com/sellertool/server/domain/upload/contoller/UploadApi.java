package com.sellertool.server.domain.upload.contoller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.message.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
public class UploadApi {
    @RequiredLogin
    @PostMapping("/s3/images")
    public ResponseEntity<?> uploadImages(@RequestParam("files") MultipartFile[] files){
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
