package com.sellertool.server.domain.product.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellertool.server.domain.product.entity.ProductEntity;
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
public class ProductVo {
    private Long cid;
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
    public static class ProductInfoVo {
        @JsonIgnore
        private Long cid;
        @JsonIgnore
        private UUID id;
        private String memo1;
        private String memo2;
        private String memo3;

        public static ProductInfoVo toVo(ProductEntity.ProductInfoEntity productInfoEntity) {
            if(productInfoEntity == null){
                return null;
            }

            ProductInfoVo vo = ProductInfoVo.builder()
                    .cid(productInfoEntity.getCid())
                    .id(productInfoEntity.getId())
                    .memo1(productInfoEntity.getMemo1())
                    .memo2(productInfoEntity.getMemo2())
                    .memo3(productInfoEntity.getMemo3())
                    .build();
            return vo;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BasicResponse {
        @JsonIgnore
        private Long cid;
        private UUID id;
        private String code;
        private String defaultName;
        private String managementName;
        private String imageUrl;
        private boolean stockManagementFlag;
        private LocalDateTime createdAt;
        @JsonIgnore
        private UUID createdBy;
        @JsonIgnore
        private Long productInfoCid;
        @JsonIgnore
        private Integer workspaceCid;
        private UUID workspaceId;
        @JsonIgnore
        private Integer categoryCid;
        private UUID categoryId;
        @JsonIgnore
        private boolean deletedFlag;

        private ProductInfoVo productInfo;

        public static BasicResponse toVo(ProductEntity productEntity, ProductEntity.ProductInfoEntity productInfoEntity) {
            if (productEntity == null) {
                return null;
            }

            BasicResponse vo = BasicResponse.builder()
                    .cid(productEntity.getCid())
                    .id(productEntity.getId())
                    .code(productEntity.getCode())
                    .defaultName(productEntity.getDefaultName())
                    .managementName(productEntity.getManagementName())
                    .imageUrl(productEntity.getImageUrl())
                    .stockManagementFlag(productEntity.isStockManagementFlag())
                    .createdAt(productEntity.getCreatedAt())
                    .createdBy(productEntity.getCreatedBy())
                    .productInfoCid(productEntity.getProductInfoCid())
                    .workspaceCid(productEntity.getWorkspaceCid())
                    .workspaceId(productEntity.getWorkspaceId())
                    .categoryCid(productEntity.getCategoryCid())
                    .categoryId(productEntity.getCategoryId())
                    .deletedFlag(productEntity.isDeletedFlag())

                    .productInfo(ProductInfoVo.toVo(productInfoEntity))
                    .build();

            return vo;
        }
    }
}
