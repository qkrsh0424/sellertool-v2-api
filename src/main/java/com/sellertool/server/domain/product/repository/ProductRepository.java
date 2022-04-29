package com.sellertool.server.domain.product.repository;

import com.sellertool.server.domain.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    List<ProductEntity> findAllByCategoryId(UUID categoryId);
    List<ProductEntity> findAllByWorkspaceIdAndCategoryId(UUID workspaceId, UUID categoryId);

    Optional<ProductEntity> findById(UUID productId);
}
