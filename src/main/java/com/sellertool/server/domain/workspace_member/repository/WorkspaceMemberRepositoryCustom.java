package com.sellertool.server.domain.workspace_member.repository;

import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepositoryCustom {
    List<WorkspaceMemberM2OJProj> qSelectM2OJByWorkspaceId(UUID workspaceId);
    Optional<WorkspaceMemberM2OJProj> qSelectM2OJ(UUID workspaceMemberId);
    Optional<WorkspaceMemberM2OJProj> qSelectM2OJ(UUID workspaceId, UUID workspaceMemberUserId);
}
