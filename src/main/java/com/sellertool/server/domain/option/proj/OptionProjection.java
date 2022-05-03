package com.sellertool.server.domain.option.proj;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.option.entity.OptionEntity;
import com.sellertool.server.domain.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionProjection {
    private OptionEntity optionEntity;
    private OptionEntity.OptionInfoEntity optionInfoEntity;
    private ProductEntity productEntity;
    private ProductEntity.ProductInfoEntity productInfoEntity;
    private CategoryEntity categoryEntity;
}
