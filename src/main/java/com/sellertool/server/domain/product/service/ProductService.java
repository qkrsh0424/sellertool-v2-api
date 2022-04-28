package com.sellertool.server.domain.product.service;

import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void saveAndModify(ProductEntity entity){
        productRepository.save(entity);
    }
}
