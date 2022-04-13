package com.sellertool.server.domain.invite_member.vo;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InviteMemberVo {
    private Integer cid;
    private UUID id;
    private UUID userId;
    private UUID workspaceId;
}
