package com.sellertool.server.domain.invite_member.proj;

import com.sellertool.server.domain.invite_member.entity.InviteMemberEntity;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteMemberM2OJProj {
    private InviteMemberEntity inviteMemberEntity;
    private UserEntity userEntity;
    private WorkspaceEntity workspaceEntity;
}
