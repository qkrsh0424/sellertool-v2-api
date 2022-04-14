package com.sellertool.server.domain.margin_record.controller;

import com.sellertool.server.domain.margin_record.dto.MarginRecordDto;
import com.sellertool.server.domain.margin_record.service.MarginRecordBusinessService;
import com.sellertool.server.domain.message.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/one")
    public ResponseEntity<?> searchMarginRecord(@RequestParam Map<String,Object> params) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(marginRecordBusinessService.searchOne(params));
        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/viewer/one")
    public ResponseEntity<?> searchViewerMarginRecord(@RequestParam Map<String,Object> params) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(marginRecordBusinessService.searchViewerOne(params));
        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchMarginRecordList(@RequestParam Map<String,Object> params) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(marginRecordBusinessService.searchListByWorkspaceId(params));
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMarginRecord(@RequestBody MarginRecordDto marginRecordDto){
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        marginRecordBusinessService.updateOne(marginRecordDto);

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMarginRecord(@RequestBody MarginRecordDto marginRecordDto) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(marginRecordBusinessService.createOneAndGet(marginRecordDto));
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteMarginRecord(@RequestBody MarginRecordDto marginRecordDto) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        marginRecordBusinessService.deleteOne(marginRecordDto);
        return new ResponseEntity<>(message, message.getStatus());
    }
}
