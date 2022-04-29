package com.sellertool.server.domain.product.repository;

import com.sellertool.server.domain.product.proj.ProductProjection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepositoryCustom {
    List<ProductProjection> qSelectListByWorkspaceIdAndCategoryId(UUID workspaceId, UUID categoryId);
    Optional<ProductProjection> qSelectM2OJById(UUID productId);

}
