package com.sellertool.server.domain.product.service;

import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.proj.ProductProjection;
import com.sellertool.server.domain.product.repository.ProductRepository;
import com.sellertool.server.utils.FlagInterface;
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

    public List<ProductProjection> qSearchListByWorkspaceIdAndCategoryId(UUID workspaceId, UUID categoryId) {
        return productRepository.qSelectListByWorkspaceIdAndCategoryId(workspaceId, categoryId);
    }

    public ProductEntity searchById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(()->new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    /**
     * Soft Delete
     * Dirty Checking Update 방식으로 진행이 되므로, 해당 메소드를 호출하기 전에 반드시 상위 메소드의 @Transactional 유무를 체크를 해줘야한다.
     * @param entity
     */
    public void logicalDeleteOne(ProductEntity entity) {
        entity.setDeletedFlag(FlagInterface.SET_DELETE);
    }

    public ProductProjection qSearchM2OJById(UUID productId) {
        return productRepository.qSelectM2OJById(productId).orElseThrow(()-> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }
}
