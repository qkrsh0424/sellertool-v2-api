package com.sellertool.server.domain.workspace_member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceMemberM2OJVo {
    private WorkspaceMember workspaceMember;
    private Workspace workspace;
    private User user;

    public static WorkspaceMemberM2OJVo toVo(WorkspaceMemberM2OJProj proj) {
        return WorkspaceMemberM2OJVo.builder()
                .workspaceMember(WorkspaceMember.toVo(proj.getWorkspaceMemberEntity()))
                .workspace(Workspace.toVo(proj.getWorkspaceEntity()))
                .user(User.toVo(proj.getUserEntity()))
                .build();
    }
}

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
class WorkspaceMember {
    @JsonIgnore
    private Integer cid;
    private UUID id;
    private UUID workspaceId;
    private UUID userId;
    private String grade;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    private String readPermissionYn;
    private String writePermissionYn;
    private String updatePermissionYn;
    private String deletePermissionYn;

    public static WorkspaceMember toVo(WorkspaceMemberEntity entity) {
        WorkspaceMember vo = WorkspaceMember.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .workspaceId(entity.getWorkspaceId())
                .userId(entity.getUserId())
                .grade(entity.getGrade())
                .createdAt(entity.getCreatedAt())
                .readPermissionYn(entity.getReadPermissionYn())
                .writePermissionYn(entity.getWritePermissionYn())
                .updatePermissionYn(entity.getUpdatePermissionYn())
                .deletePermissionYn(entity.getDeletePermissionYn())
                .build();
        return vo;
    }
}

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Workspace {
    private UUID id;
    private String name;
    private String publicYn;
    private String deleteProtectionYn;
    private UUID masterId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    public static Workspace toVo(WorkspaceEntity entity) {
        Workspace vo = Workspace.builder()
                .id(entity.getId())
                .name(entity.getName())
                .publicYn(entity.getPublicYn())
                .deleteProtectionYn(entity.getDeleteProtectionYn())
                .masterId(entity.getMasterId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        return vo;
    }
}

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
class User{
    private UUID id;
    private String username;
    private String email;
    private String nickname;
    private String roles;

    public static User toVo(UserEntity entity) {
        User vo = User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .roles(entity.getRoles())
                .build();
        return vo;
    }
}