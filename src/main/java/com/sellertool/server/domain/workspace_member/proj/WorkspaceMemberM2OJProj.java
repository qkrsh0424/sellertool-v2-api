package com.sellertool.server.domain.workspace_member.proj;

import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceMemberM2OJProj {
    private WorkspaceMemberEntity workspaceMemberEntity;
    private WorkspaceEntity workspaceEntity;
    private UserEntity userEntity;
}
