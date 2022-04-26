package com.sellertool.server.domain.category.entity;

import com.sellertool.server.domain.category.dto.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

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

    @Column(name = "workspace_id")
    @Type(type = "uuid-char")
    private UUID workspaceId;

    public static CategoryEntity toEntity(CategoryDto dto) {
        CategoryEntity entity = CategoryEntity.builder()
                .cid(dto.getCid())
                .id(dto.getId())
                .name(dto.getName())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .workspaceId(dto.getWorkspaceId())
                .build();
        return entity;
    }
}
