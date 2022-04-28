package com.sellertool.server.domain.product.entity;

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
@Table(name = "product")
@DynamicInsert
@DynamicUpdate
@Where(clause = "deleted_flag=0") // Sort Delete 적용
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "default_name")
    private String defaultName;

    @Column(name = "management_name")
    private String managementName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "memo1")
    private String memo1;

    @Column(name = "memo2")
    private String memo2;

    @Column(name = "memo3")
    private String memo3;

    @Column(name = "stock_management_flag")
    private boolean stockManagementFlag;

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

    @Column(name = "category_cid")
    private Integer categoryCid;

    @Column(name = "category_id")
    @Type(type = "uuid-char")
    private UUID categoryId;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;
}
