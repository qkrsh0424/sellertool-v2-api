package com.sellertool.server.domain.category.repository;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findAllByWorkspaceId(UUID workspaceId);
    Optional<CategoryEntity> findById(UUID id);
}
