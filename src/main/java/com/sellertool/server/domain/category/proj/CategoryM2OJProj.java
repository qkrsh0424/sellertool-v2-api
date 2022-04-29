package com.sellertool.server.domain.category.proj;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryM2OJProj {
    CategoryEntity categoryEntity;
    WorkspaceEntity workspaceEntity;
    WorkspaceMemberEntity workspaceMemberEntity;
    UserEntity userEntity;
}
