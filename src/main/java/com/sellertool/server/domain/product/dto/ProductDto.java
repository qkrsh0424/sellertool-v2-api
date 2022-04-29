package com.sellertool.server.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long cid;
    private UUID id;
    private String code;
    private String defaultName;
    private String managementName;
    private String imageUrl;
    private boolean stockManagementFlag;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Long productInfoCid;
    private UUID productInfoId;
    private Integer workspaceCid;
    private UUID workspaceId;
    private Integer categoryCid;
    private UUID categoryId;
    private boolean deletedFlag;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductInfoDto{
        private Long cid;
        private UUID id;
        private String memo1;
        private String memo2;
        private String memo3;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateRequest{
        private Long cid;
        private UUID id;
        private String code;
        private String defaultName;
        private String managementName;
        private String imageUrl;
        private boolean stockManagementFlag;
        private LocalDateTime createdAt;
        private UUID createdBy;
        private Long productInfoCid;
        private Integer workspaceCid;
        private UUID workspaceId;
        private Integer categoryCid;
        private UUID categoryId;
        private boolean deletedFlag;

        /*
        product_info fields
         */
        private String memo1;
        private String memo2;
        private String memo3;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateRequest{
        private Long cid;
        private UUID id;
        private String code;
        private String defaultName;
        private String managementName;
        private String imageUrl;
        private boolean stockManagementFlag;
        private LocalDateTime createdAt;
        private UUID createdBy;
        private Long productInfoCid;
        private Integer workspaceCid;
        private UUID workspaceId;
        private Integer categoryCid;
        private UUID categoryId;
        private boolean deletedFlag;

        private ProductInfoDto productInfo;
    }
}
