package com.sellertool.server.domain.category.repository;

import com.sellertool.server.domain.category.proj.CategoryM2OJProj;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepositoryCustom {
    Optional<CategoryM2OJProj> qSelectM2OJ(UUID categoryId, UUID userId);
}
