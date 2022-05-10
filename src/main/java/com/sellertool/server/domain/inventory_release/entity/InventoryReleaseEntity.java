package com.sellertool.server.domain.inventory_release.entity;

import com.sellertool.server.domain.inventory_release.dto.InventoryReleaseGetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory_release")
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReleaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Long cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "release_unit")
    private Integer releaseUnit;

    @Column(name = "memo")
    private String memo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "product_option_cid")
    private Long optionCid;

    @Column(name = "product_option_id")
    @Type(type ="uuid-char")
    private UUID optionId;

    @Column(name = "erp_order_item_id")
    @Type(type ="uuid-char")
    private UUID erpOrderItemId;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseGetDto => ProductReleaseEntity
     * 
     * @param dto : ProductReleaseGetDto
     * @param userId : UUID
     * @return ProductReleaseEntity
     */
    public static InventoryReleaseEntity toEntity(InventoryReleaseGetDto dto) {
        InventoryReleaseEntity entity = InventoryReleaseEntity.builder()
              .id(UUID.randomUUID())
              .releaseUnit(dto.getReleaseUnit())
              .memo(dto.getMemo())
              .createdAt(dto.getCreatedAt())
              .createdBy(dto.getCreatedBy())
              .optionCid(dto.getProductOptionCid())
              .optionId(dto.getProductOptionId())
              .erpOrderItemId(dto.getErpOrderItemId())
              .build();

        return entity;
    }
}
