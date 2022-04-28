package com.sellertool.server.domain.product.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellertool.server.domain.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVo {
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BasicResponse {
        @JsonIgnore
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
        @JsonIgnore
        private UUID createdBy;
        private Integer workspaceCid;
        private UUID workspaceId;
        private Integer categoryCid;
        private UUID categoryId;
        @JsonIgnore
        private boolean deletedFlag;

        public static ProductVo.BasicResponse toVo(ProductEntity productEntity) {
            if(productEntity == null){
                return null;
            }
            ProductVo.BasicResponse vo = ProductVo.BasicResponse.builder()
                    .cid(productEntity.getCid())
                    .id(productEntity.getId())
                    .code(productEntity.getCode())
                    .defaultName(productEntity.getDefaultName())
                    .managementName(productEntity.getManagementName())
                    .imageUrl(productEntity.getImageUrl())
                    .memo1(productEntity.getMemo1())
                    .memo2(productEntity.getMemo2())
                    .memo3(productEntity.getMemo3())
                    .stockManagementFlag(productEntity.isStockManagementFlag())
                    .createdAt(productEntity.getCreatedAt())
                    .createdBy(productEntity.getCreatedBy())
                    .workspaceCid(productEntity.getWorkspaceCid())
                    .workspaceId(productEntity.getWorkspaceId())
                    .categoryCid(productEntity.getCategoryCid())
                    .categoryId(productEntity.getCategoryId())
                    .deletedFlag(productEntity.isDeletedFlag())
                    .build();

            return vo;
        }
    }
}
