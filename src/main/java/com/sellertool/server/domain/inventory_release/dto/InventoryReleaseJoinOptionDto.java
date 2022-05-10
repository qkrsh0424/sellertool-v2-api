package com.sellertool.server.domain.inventory_release.dto;

import com.sellertool.server.domain.option.dto.OptionDto;
import com.sellertool.server.domain.product.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class InventoryReleaseJoinOptionDto {
    InventoryReleaseGetDto release;
    OptionDto option;
    ProductDto product;
    
    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseProj => ProductReleaseJoinOptionDto
     * 
     * @param projs : ProductReleaseProj
     * @return ProductReleaseJoinOptionDto
     */
//    public static InventoryReleaseJoinOptionDto toDto(InventoryReleaseProjection proj) {
//        InventoryReleaseJoinOptionDto dto = InventoryReleaseJoinOptionDto.builder()
//            .release(InventoryReleaseGetDto.toDto(proj.getInventoryReleaseEntity()))
//            .option(OptionDto.toDto(proj.getProductOption()))
//            .product(ProductGetDto.toDto(proj.getProduct()))
//            .build();
//
//        return dto;
//    }
}
