package com.sellertool.server.domain.category.entity;

import com.sellertool.server.domain.category.dto.CategoryDto;
import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "category")
@DynamicUpdate
@DynamicInsert
@Where(clause = "deleted_flag=0") // Sort Delete 적용
//@SQLDelete("UPDATE category SET deleted_flag=1 WHERE id = ?") // deleteById 를 발생 시켰을때 소프트 딜리트 제공
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    @Type(type = "uuid-char")
    private UUID createdBy;

    @Column(name = "workspace_cid")
    private Integer workspaceCid;

    @Column(name = "workspace_id")
    @Type(type = "uuid-char")
    private UUID workspaceId;

    @Column(name="deleted_flag")
    private boolean deletedFlag;

    public static CategoryEntity toEntity(CategoryDto dto) {
        CategoryEntity entity = CategoryEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .name(dto.getName())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .workspaceCid(dto.getWorkspaceCid())
                .workspaceId(dto.getWorkspaceId())
                .deletedFlag(dto.isDeletedFlag())
                .build();
        return entity;
    }

    public static void belongInWorkspace(CategoryEntity categoryEntity, WorkspaceEntity workspaceEntity) {
        if(categoryEntity == null || workspaceEntity == null){
            throw new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다.");
        }

        if(!categoryEntity.workspaceId.equals(workspaceEntity.getId())){
            throw new AccessDeniedPermissionException("해당 데이터에 접근 권한이 없습니다.");
        }
    }
}
