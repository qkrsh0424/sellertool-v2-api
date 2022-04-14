package com.sellertool.server.domain.workspace_member.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceMemberVo {
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

    public static WorkspaceMemberVo toVo(WorkspaceMemberEntity entity) {
        WorkspaceMemberVo vo = WorkspaceMemberVo.builder()
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
