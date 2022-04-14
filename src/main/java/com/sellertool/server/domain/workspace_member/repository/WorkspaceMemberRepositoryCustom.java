package com.sellertool.server.domain.workspace_member.repository;

import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepositoryCustom {
    List<WorkspaceMemberM2OJProj> qSelectM2OJByWorkspaceId(UUID workspaceId);
}
