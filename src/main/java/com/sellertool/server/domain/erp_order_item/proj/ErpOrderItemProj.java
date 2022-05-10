package com.sellertool.server.domain.erp_order_item.proj;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.sellertool.server.domain.option.entity.OptionEntity;
import com.sellertool.server.domain.product.entity.ProductEntity;
import lombok.Getter;

@Getter
public class ErpOrderItemProj {
    ErpOrderItemEntity erpOrderItemEntity;
    ProductEntity productEntity;
    OptionEntity optionEntity;
    CategoryEntity categoryEntity;
}
