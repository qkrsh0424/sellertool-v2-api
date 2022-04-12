package com.sellertool.server.domain.workspace_member.controller;

import com.sellertool.server.domain.message.dto.Message;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workspace-members")
@RequiredArgsConstructor
public class WorkspaceMemberApiV1 {
    private final WorkspaceMemberBusinessService workspaceMemberBusinessService;

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<?> searchListByWorkspaceId(@PathVariable(value = "workspaceId") Object workspaceIdObj) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(workspaceMemberBusinessService.searchListByWorkspaceId(workspaceIdObj));

        return new ResponseEntity<>(message, message.getStatus());
    }
}
