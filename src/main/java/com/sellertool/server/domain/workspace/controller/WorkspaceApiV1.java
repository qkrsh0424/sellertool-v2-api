package com.sellertool.server.domain.workspace.controller;

import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.message.dto.Message;
import com.sellertool.server.domain.workspace.dto.WorkspaceDto;
import com.sellertool.server.domain.workspace.service.WorkspaceBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceApiV1 {
    private final WorkspaceBusinessService workspaceBusinessService;

    @GetMapping("/{id}")
    public ResponseEntity<?> searchWorkspace(@PathVariable(value = "id") Object workspaceIdObj) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(workspaceBusinessService.searchWorkspace(workspaceIdObj));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("")
    public ResponseEntity<?> searchWorkspaces() {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(workspaceBusinessService.searchListByUser());

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/private")
    public ResponseEntity<?> createPrivate(@RequestBody WorkspaceDto dto) {
        Message message = new Message();

        message.setData(workspaceBusinessService.createPrivate(dto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PatchMapping("/{workspaceId}/name")
    public ResponseEntity<?> changeNameByWorkspaceId(@PathVariable(value = "workspaceId") Object workspaceIdObj, @RequestBody Map<String, Object> body) {
        Message message = new Message();

        UUID workspaceId = null;
        try {
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("형식이 맞지 않습니다.");
        }
        workspaceBusinessService.changeNameByWorkspaceId(workspaceId, body);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
