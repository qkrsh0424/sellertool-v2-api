package com.sellertool.server.domain.workspace_member.entity;

import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.exception.dto.NotAllowedAccessException;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "workspace_member")
@DynamicInsert
@DynamicUpdate
public class WorkspaceMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;
    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;
    @Column(name = "workspace_id")
    @Type(type = "uuid-char")
    private UUID workspaceId;
    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userId;
    @Column(name = "grade")
    private String grade;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "read_permission_yn", columnDefinition = "y")
    private String readPermissionYn;
    @Column(name = "write_permission_yn", columnDefinition = "y")
    private String writePermissionYn;
    @Column(name = "update_permission_yn", columnDefinition = "n")
    private String updatePermissionYn;
    @Column(name = "delete_permission_yn", columnDefinition = "n")
    private String deletePermissionYn;

    /**
     * With Entities
     *
     * @param entities
     * @param userId
     */
    public static void memberOnlyWE(List<WorkspaceMemberEntity> entities, UUID userId) {
        entities.stream().filter(r -> r.getUserId().equals(userId)).findFirst().orElseThrow(() -> new NotAllowedAccessException("접근 권한이 없습니다."));
    }

    public static void memberOnlyWE(WorkspaceMemberEntity workspaceMemberEntity) {
        if(workspaceMemberEntity == null){
            throw new NotAllowedAccessException("접근 권한이 없습니다.");
        }
    }
    /**
     * With Projections
     *
     * @param proj
     * @param userId
     */
    public static void memberOnlyWP(List<WorkspaceMemberM2OJProj> proj, UUID userId) {
        List<WorkspaceMemberEntity> entities = proj.stream().map(r -> r.getWorkspaceMemberEntity()).collect(Collectors.toList());
        memberOnlyWE(entities, userId);
    }

    public static void hasWritePermission(WorkspaceMemberEntity entity) {
        if (!entity.getWritePermissionYn().equals("y")) {
            throw new AccessDeniedPermissionException("해당 기능에 접근 권한이 없습니다.");
        }
    }

    public static void hasUpdatePermission(WorkspaceMemberEntity entity) {
        if (!entity.getUpdatePermissionYn().equals("y")) {
            throw new AccessDeniedPermissionException("해당 기능에 접근 권한이 없습니다.");
        }
    }

    public static void hasDeletePermission(WorkspaceMemberEntity entity) {
        if (!entity.getDeletePermissionYn().equals("y")) {
            throw new AccessDeniedPermissionException("해당 기능에 접근 권한이 없습니다.");
        }
    }
}
