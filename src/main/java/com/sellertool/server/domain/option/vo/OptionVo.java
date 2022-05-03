package com.sellertool.server.domain.option.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sellertool.server.domain.option.entity.OptionEntity;
import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.vo.ProductVo;
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
public class OptionVo {
    private Long cid;
    private UUID id;
    private String code;
    private String defaultName;
    private String managementName;
    private String imageUrl;
    private Integer stockUnit;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Long optionInfoCid;
    private Long productCid;
    private UUID productId;
    private Integer workspaceCid;
    private UUID workspaceId;
    private boolean deletedFlag;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OptionInfoVo {
        @JsonIgnore
        private Long cid;
        @JsonIgnore
        private UUID id;
        private String status;
        private Integer salesPrice;
        private String memo1;
        private String memo2;
        private String memo3;

        public static OptionInfoVo toVo(OptionEntity.OptionInfoEntity entity){
            OptionInfoVo vo = OptionInfoVo.builder()
                    .cid(entity.getCid())
                    .id(entity.getId())
                    .status(entity.getStatus())
                    .salesPrice(entity.getSalesPrice())
                    .memo1(entity.getMemo1())
                    .memo2(entity.getMemo2())
                    .memo3(entity.getMemo3())
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
        private Integer stockUnit;
        private LocalDateTime createdAt;
        @JsonIgnore
        private UUID createdBy;
        @JsonIgnore
        private Long optionInfoCid;
        @JsonIgnore
        private Long productCid;
        private UUID productId;
        @JsonIgnore
        private Integer workspaceCid;
        private UUID workspaceId;
        @JsonIgnore
        private boolean deletedFlag;

        private OptionInfoVo optionInfo;

        public static OptionVo.BasicResponse toVo(OptionEntity optionEntity, OptionEntity.OptionInfoEntity optionInfoEntity) {
            if (optionEntity == null) {
                return null;
            }

            OptionVo.BasicResponse vo = OptionVo.BasicResponse.builder()
                    .cid(optionEntity.getCid())
                    .id(optionEntity.getId())
                    .code(optionEntity.getCode())
                    .defaultName(optionEntity.getDefaultName())
                    .managementName(optionEntity.getManagementName())
                    .imageUrl(optionEntity.getImageUrl())
                    .stockUnit(optionEntity.getStockUnit())
                    .createdAt(optionEntity.getCreatedAt())
                    .createdBy(optionEntity.getCreatedBy())
                    .optionInfoCid(optionEntity.getOptionInfoCid())
                    .productCid(optionEntity.getProductCid())
                    .productId(optionEntity.getProductId())
                    .workspaceCid(optionEntity.getWorkspaceCid())
                    .workspaceId(optionEntity.getWorkspaceId())
                    .deletedFlag(optionEntity.isDeletedFlag())

                    .optionInfo(OptionVo.OptionInfoVo.toVo(optionInfoEntity))
                    .build();

            return vo;
        }
    }
}
