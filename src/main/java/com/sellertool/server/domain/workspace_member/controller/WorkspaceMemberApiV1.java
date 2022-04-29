package com.sellertool.server.domain.workspace_member.controller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.message.dto.Message;
import com.sellertool.server.domain.workspace_member.dto.WorkspaceMemberDto;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequiredLogin
    @DeleteMapping("/{workspaceMemberId}/workspaces/{workspaceId}")
    public ResponseEntity<?> deleteByIdAndWorkspaceId(@PathVariable(value = "workspaceMemberId") Object workspaceMemberIdObj, @PathVariable(value = "workspaceId") Object workspaceIdObj) {
        Message message = new Message();

        UUID workspaceMemberId = null;
        UUID workspaceId = null;
        try{
            workspaceMemberId = UUID.fromString(workspaceMemberIdObj.toString());
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("요청이 잘못 되었습니다.");
        }

        workspaceMemberBusinessService.deleteByIdAndWorkspaceId(workspaceMemberId, workspaceId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
//        message.setData(workspaceMemberBusinessService.searchListByWorkspaceId(workspaceId));

        return new ResponseEntity<>(message, message.getStatus());
    }

    @RequiredLogin
    @PatchMapping("/{workspaceMemberId}/permissions")
    public ResponseEntity<?> changePermissions(@PathVariable(value = "workspaceMemberId") Object workspaceMemberIdObj, @RequestBody WorkspaceMemberDto workspaceMemberDto){
        Message message = new Message();

        UUID workspaceMemberId = null;
        try{
            workspaceMemberId = UUID.fromString(workspaceMemberIdObj.toString());
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("요청이 잘못 되었습니다.");
        }

        workspaceMemberBusinessService.changePermissions(workspaceMemberId, workspaceMemberDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
