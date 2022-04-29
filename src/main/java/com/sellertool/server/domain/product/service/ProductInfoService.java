package com.sellertool.server.domain.product.service;

import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInfoService {
    private final ProductInfoRepository productInfoRepository;

    public ProductEntity.ProductInfoEntity saveAndGet(ProductEntity.ProductInfoEntity entity){
        return productInfoRepository.save(entity);
    }
}
