package com.sellertool.server.domain.workspace.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceDto {
    private Integer cid;
    private UUID id;
    private String name;
    private UUID masterId;
    private String publicYn;
    private String deleteProtectionYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
