package com.sellertool.server.domain.workspace_member.repository;

import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMemberEntity, Integer>, WorkspaceMemberRepositoryCustom {
    List<WorkspaceMemberEntity> findByWorkspaceId(UUID workspaceId);
    List<WorkspaceMemberEntity> findByUserId(UUID userId);
}
