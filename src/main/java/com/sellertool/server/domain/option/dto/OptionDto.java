package com.sellertool.server.domain.option.dto;

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
public class OptionDto {
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
    public static class OptionInfoDto {
        private Long cid;
        private UUID id;
        private String status;
        private Integer salesPrice;
        private String memo1;
        private String memo2;
        private String memo3;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateRequest {
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

        private OptionInfoDto optionInfo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateRequest {
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

        private OptionInfoDto optionInfo;
    }
}
