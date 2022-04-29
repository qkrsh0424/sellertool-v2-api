package com.sellertool.server.domain.product.repository;

import com.sellertool.server.domain.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductEntity.ProductInfoEntity, Long> {
}
