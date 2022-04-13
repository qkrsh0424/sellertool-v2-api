package com.sellertool.server.domain.invite_member.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InviteMemberDto {
    private String username;
    private UUID workspaceId;
}
