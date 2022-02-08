package com.sellertool.server.domain.margin_record.controller;

import com.sellertool.server.domain.margin_record.dto.MarginRecordDto;
import com.sellertool.server.domain.margin_record.service.MarginRecordBusinessService;
import com.sellertool.server.domain.message.model.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/margin-record")
public class MarginRecordApiV1 {
    private final MarginRecordBusinessService marginRecordBusinessService;

    @Autowired
    public MarginRecordApiV1(
            MarginRecordBusinessService marginRecordBusinessService
    ) {
        this.marginRecordBusinessService = marginRecordBusinessService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMarginRecord(@RequestBody MarginRecordDto marginRecordDto) {
        Message message = new Message();
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        marginRecordBusinessService.createOne(marginRecordDto);
        return new ResponseEntity<>(message, message.getStatus());
    }
}
