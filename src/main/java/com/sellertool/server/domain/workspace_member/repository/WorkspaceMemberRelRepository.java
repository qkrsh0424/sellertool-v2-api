package com.sellertool.server.domain.workspace_member.repository;

import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberRelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceMemberRelRepository extends JpaRepository<WorkspaceMemberRelEntity, Integer> {
}
