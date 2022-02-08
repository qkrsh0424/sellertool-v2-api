package com.sellertool.server.domain.workspace.vo;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static WorkspaceVo toVo(WorkspaceEntity entity) {
        WorkspaceVo vo = WorkspaceVo.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
        return vo;
    }
}
