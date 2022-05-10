package com.sellertool.server.domain.inventory_release.dto;

import com.sellertool.server.domain.category.dto.CategoryDto;
import com.sellertool.server.domain.option.dto.OptionDto;
import com.sellertool.server.domain.product.dto.ProductDto;
import com.sellertool.server.domain.user.vo.UserVo;
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
public class InventoryReleaseJoinResDto {
    InventoryReleaseGetDto release;
    ProductDto product;
    CategoryDto category;
    UserVo user;
    OptionDto option;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseProj => ProductReleaseJoinResDto
     * 
     * @param projs : ProductReleaseProj
     * @return ProductReleaseJoinResDto
     */
//    public static InventoryReleaseJoinResDto toDto(InventoryReleaseProjection proj) {
//        InventoryReleaseJoinResDto dto = InventoryReleaseJoinResDto.builder()
//            .release(InventoryReleaseGetDto.toDto(proj.getInventoryReleaseEntity()))
//            .option(ProductOptionGetDto.toDto(proj.getProductOption()))
//            .product(ProductGetDto.toDto(proj.getProduct()))
//            .category(ProductCategoryGetDto.toDto(proj.getCategory()))
//            .user(UserGetDto.toDto(proj.getUser()))
//            .build();
//
//        return dto;
//    }
}
