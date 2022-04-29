package com.sellertool.server.domain.upload.contoller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.message.dto.Message;
import com.sellertool.server.domain.upload.service.UploadBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadApi {
    private final UploadBusinessService uploadBusinessService;

    @RequiredLogin
    @PostMapping("/s3/images")
    public ResponseEntity<?> uploadImages(@RequestParam("files") MultipartFile[] files){
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(uploadBusinessService.uploadImagesToS3(files));

        return new ResponseEntity<>(message, message.getStatus());
    }

}
