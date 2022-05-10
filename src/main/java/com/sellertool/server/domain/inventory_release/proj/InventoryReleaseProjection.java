package com.sellertool.server.domain.inventory_release.proj;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.inventory_release.entity.InventoryReleaseEntity;
import com.sellertool.server.domain.option.entity.OptionEntity;
import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.user.entity.UserEntity;

/**
 * 자기 자신과 Many To One 관계에 놓여있는 값들과 JOIN
 */
public interface InventoryReleaseProjection {
    InventoryReleaseEntity getInventoryReleaseEntity();
    OptionEntity getOptionEntity();
    ProductEntity getProductEntity();
    CategoryEntity getCategoryEntity();
    UserEntity getUserEntity();
}
