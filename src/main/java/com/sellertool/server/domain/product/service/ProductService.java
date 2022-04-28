package com.sellertool.server.domain.product.service;

import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void saveAndModify(ProductEntity entity){
        productRepository.save(entity);
    }

    public List<ProductEntity> searchListByCategoryId(UUID categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    public List<ProductEntity> searchListByWorkspaceIdAndCategoryId(UUID workspaceId, UUID categoryId) {
        return productRepository.findAllByWorkspaceIdAndCategoryId(workspaceId, categoryId);
    }
}
