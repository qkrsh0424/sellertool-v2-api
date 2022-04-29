package com.sellertool.server.domain.option.entity;

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
@Table(name = "option")
@DynamicInsert
@DynamicUpdate
@Where(clause = "deleted_flag=0")
public class OptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "id")
    @Type(type="uuid-char")
    private UUID id;

    @Column(name = "code")
    private String code;

    @Column(name = "default_name")
    private String defaultName;

    @Column(name = "management_name")
    private String managementName;

    @Column(name = "stock_unit")
    private Integer stockUnit;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    @Type(type="uuid-char")
    private UUID createdBy;

    @Column(name = "option_info_cid")
    private Long optionInfoCid;

    @Column(name = "product_cid")
    private Long productCid;

    @Column(name = "product_id")
    @Type(type="uuid-char")
    private UUID productId;

    @Column(name = "workspace_cid")
    private Long workspaceCid;

    @Column(name = "workspace_id")
    @Type(type="uuid-char")
    private UUID workspaceId;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;
}
