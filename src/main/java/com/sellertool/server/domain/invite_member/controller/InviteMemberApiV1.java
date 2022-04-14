package com.sellertool.server.domain.invite_member.controller;

import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.invite_member.dto.InviteMemberDto;
import com.sellertool.server.domain.invite_member.service.InviteMemberBusinessService;
import com.sellertool.server.domain.message.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invite-members")
@RequiredArgsConstructor
public class InviteMemberApiV1 {
    private final InviteMemberBusinessService inviteMemberBusinessService;

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<?> searchByWorkspaceId(@PathVariable(value = "workspaceId") UUID workspaceId) {
        Message message = new Message();

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(inviteMemberBusinessService.searchM2OJByWorkspaceId(workspaceId));
        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/one")
    public ResponseEntity<?> createOne(@RequestBody InviteMemberDto inviteMemberDto) {
        Message message = new Message();

        inviteMemberBusinessService.createOne(inviteMemberDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/workspaces/{workspaceId}/invite-members/{inviteMemberId}")
    public ResponseEntity<?> deleteByWorkspaceIdAndInviteMemberId(
            @PathVariable(value = "workspaceId") Object workspaceIdObj,
            @PathVariable(value = "inviteMemberId") Object inviteMemberIdObj
    ) {
        Message message = new Message();
        UUID workspaceId = null;
        UUID inviteMemberId = null;

        try {
            workspaceId = UUID.fromString(workspaceIdObj.toString());
            inviteMemberId = UUID.fromString(inviteMemberIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("형식이 올바르지 않습니다.");
        }

        inviteMemberBusinessService.deleteByWorkspaceIdAndInviteMemberId(workspaceId, inviteMemberId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
