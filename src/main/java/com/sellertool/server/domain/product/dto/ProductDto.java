package com.sellertool.server.domain.product.dto;

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
public class ProductDto {
    private Integer cid;
    private UUID id;
    private String code;
    private String defaultName;
    private String managementName;
    private String imageUrl;
    private String memo1;
    private String memo2;
    private String memo3;
    private boolean stockManagementFlag;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Integer workspaceCid;
    private UUID workspaceId;
    private Integer categoryCid;
    private UUID categoryId;
    private boolean deletedFlag;
}
