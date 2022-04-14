package com.sellertool.server.domain.workspace.repository;

import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Integer>, WorkspaceRepositoryCustom {
    @Query("SELECT w FROM WorkspaceEntity w WHERE w.id IN :workspaceIds")
    List<WorkspaceEntity> findAllByIds(List<UUID> workspaceIds);
}
