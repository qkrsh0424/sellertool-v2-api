package com.sellertool.server.domain.workspace.entity;

import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "workspace")
@DynamicInsert
public class WorkspaceRelEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;
    @Column(name = "id", unique = true)
    @Type(type = "uuid-char")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "master_id")
    @Type(type = "uuid-char")
    private UUID masterId;
    @Column(name = "public_yn", columnDefinition = "n")
    private String publicYn;
    @Column(name = "delete_protection_yn", columnDefinition = "y")
    private String deleteProtectionYn;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public static void existence(WorkspaceRelEntity workspaceEntity) {
        if (workspaceEntity == null) {
            throw new NotMatchedFormatException("워크스페이스를 찾을 수 없음.");
        }
    }

    public static void existence(Optional<WorkspaceRelEntity> workspaceEntityOpt){
        WorkspaceRelEntity workspaceEntity = workspaceEntityOpt.orElse(null);
        existence(workspaceEntity);
    }

    public static void masterOnly(WorkspaceRelEntity workspaceEntity, UUID userId) {
        if (!workspaceEntity.getMasterId().equals(userId)) {
            throw new AccessDeniedPermissionException("워크스페이스 마스터에 접근 권한이 없습니다.");
        }
    }

    public static void publicOnly(WorkspaceRelEntity workspaceEntity) {
        if (!workspaceEntity.getPublicYn().equals("y")) {
            throw new AccessDeniedPermissionException("워크스페이스가 퍼블릭 상태가 아닙니다.");
        }
    }
}
