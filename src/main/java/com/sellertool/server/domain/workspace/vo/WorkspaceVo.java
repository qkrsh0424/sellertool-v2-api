package com.sellertool.server.domain.workspace.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
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
public class WorkspaceVo {
    private UUID id;
    private String name;
    private String publicYn;
    private String deleteProtectionYn;
    private UUID masterId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;

    public static WorkspaceVo toVo(WorkspaceEntity entity) {
        WorkspaceVo vo = WorkspaceVo.builder()
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
