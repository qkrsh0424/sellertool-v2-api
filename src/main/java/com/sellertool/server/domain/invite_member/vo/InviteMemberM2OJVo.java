package com.sellertool.server.domain.invite_member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sellertool.server.domain.invite_member.entity.InviteMemberEntity;
import com.sellertool.server.domain.invite_member.proj.InviteMemberM2OJProj;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteMemberM2OJVo {
    private Integer cid;
    private UUID id;
    private UUID userId;
    private UUID workspaceId;

    private User user;
    private Workspace workspace;

    public static InviteMemberM2OJVo toVo(InviteMemberM2OJProj proj) {
        InviteMemberM2OJVo vo = InviteMemberM2OJVo.builder()
                .cid(proj.getInviteMemberEntity().getCid())
                .id(proj.getInviteMemberEntity().getId())
                .userId(proj.getInviteMemberEntity().getUserId())
                .workspaceId(proj.getInviteMemberEntity().getWorkspaceId())

                .user(User.toVo(proj.getUserEntity()))
                .workspace(Workspace.toVo(proj.getWorkspaceEntity()))
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
class InviteMember {
    private Integer cid;
    private UUID id;
    private UUID userId;
    private UUID workspaceId;

    public static InviteMember toVo(InviteMemberEntity entity) {
        InviteMember vo = InviteMember.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .userId(entity.getUserId())
                .workspaceId(entity.getWorkspaceId())
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
class User {
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
