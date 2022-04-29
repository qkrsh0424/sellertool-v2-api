package com.sellertool.server.domain.workspace.repository;

import com.sellertool.server.domain.workspace.entity.WorkspaceRelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRelRepository extends JpaRepository<WorkspaceRelEntity, Integer> {
}
