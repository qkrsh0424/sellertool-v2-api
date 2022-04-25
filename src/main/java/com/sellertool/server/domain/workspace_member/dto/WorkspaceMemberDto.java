package com.sellertool.server.domain.workspace_member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceMemberDto {
    private Integer cid;
    private UUID id;
    private UUID workspaceId;
    private UUID userId;
    private String grade;
    private LocalDateTime createdAt;
    private String readPermissionYn;
    private String writePermissionYn;
    private String updatePermissionYn;
    private String deletePermissionYn;
}
