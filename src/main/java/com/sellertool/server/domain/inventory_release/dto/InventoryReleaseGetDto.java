package com.sellertool.server.domain.inventory_release.dto;

import com.sellertool.server.domain.inventory_release.entity.InventoryReleaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReleaseGetDto {
    private Long cid;
    private UUID id;
    private Integer releaseUnit;
    private String memo;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Long productOptionCid;
    private UUID productOptionId;
    private UUID erpOrderItemId;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseEntity => ProductReleaseGetDto
     * 
     * @param entity : ProductReleaseEntity
     * @return ProductReleaseGetDto
     */
    public static InventoryReleaseGetDto toDto(InventoryReleaseEntity entity) {
        InventoryReleaseGetDto dto = InventoryReleaseGetDto.builder()
           .cid(entity.getCid())
           .id(entity.getId())
           .releaseUnit(entity.getReleaseUnit())
           .memo(entity.getMemo())
           .createdAt(entity.getCreatedAt())
           .createdBy(entity.getCreatedBy())
           .productOptionCid(entity.getOptionCid())
           .productOptionId(entity.getOptionId())
           .erpOrderItemId(entity.getErpOrderItemId())
           .build();

        return dto;
    }
}
