package com.sellertool.server.domain.workspace.service;

import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceService(
            WorkspaceRepository workspaceRepository
    ) {
        this.workspaceRepository = workspaceRepository;
    }

    public void saveAndModify(WorkspaceEntity workspaceEntity) {
        workspaceRepository.save(workspaceEntity);
    }

    public WorkspaceEntity searchOwnPrivateWorkspace(UUID userId) {
        return workspaceRepository.qSelectOwnWorkspace(userId).stream().findFirst().orElse(null);
    }

    public WorkspaceEntity searchWorkspaceOne(UUID workspaceId){
        WorkspaceEntity workspaceEntity = workspaceRepository.findById(workspaceId).orElse(null);
        return workspaceEntity;
    }

    public List<WorkspaceEntity> searchListByUserId(UUID userId) {
        return workspaceRepository.qSelectListByUserId(userId);
    }

    public List<WorkspaceEntity> searchListByIds(List<UUID> workspaceIds) {
        return workspaceRepository.findAllByIds(workspaceIds);
    }
}
