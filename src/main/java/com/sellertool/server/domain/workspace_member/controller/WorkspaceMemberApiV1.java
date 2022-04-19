package com.sellertool.server.domain.workspace_member.controller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
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

    @RequiredLogin
    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<?> searchListByWorkspaceId(@PathVariable(value = "workspaceId") Object workspaceIdObj) {
        Message message = new Message();

        UUID workspaceId = null;
        try{
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("요청이 잘못 되었습니다.");
        }

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(workspaceMemberBusinessService.searchListByWorkspaceId(workspaceId));

        return new ResponseEntity<>(message, message.getStatus());
    }
}
