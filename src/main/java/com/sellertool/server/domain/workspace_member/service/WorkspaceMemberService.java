package com.sellertool.server.domain.workspace_member.service;

import com.sellertool.server.domain.exception.dto.NotAllowedAccessException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import com.sellertool.server.domain.workspace_member.repository.WorkspaceMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceMemberService {
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Autowired
    public WorkspaceMemberService(
            WorkspaceMemberRepository workspaceMemberRepository
    ) {
        this.workspaceMemberRepository = workspaceMemberRepository;
    }

    public void saveAndModify(WorkspaceMemberEntity entity) {
        workspaceMemberRepository.save(entity);
    }

    public List<WorkspaceMemberEntity> searchListByWorkspaceId(UUID workspaceId) {
        return workspaceMemberRepository.findByWorkspaceId(workspaceId);
    }

    public List<WorkspaceMemberM2OJProj> searchM2OJProjectionsByWorkspaceId(UUID workspaceId) {
        return workspaceMemberRepository.qSelectM2OJByWorkspaceId(workspaceId);
    }

    public WorkspaceMemberM2OJProj searchM2OJProjection(UUID workspaceMemberId) {
        return workspaceMemberRepository.qSelectM2OJ(workspaceMemberId).orElseThrow(()-> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    public WorkspaceMemberM2OJProj searchM2OJProjection(UUID workspaceId, UUID workspaceMemberUserId) {
        return workspaceMemberRepository.qSelectM2OJ(workspaceId, workspaceMemberUserId).orElseThrow(()-> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    public boolean isAccessedWritePermissionOfWorkspace(UUID workspaceId, UUID userId) {
        List<WorkspaceMemberEntity> workspaceMemberEntities = workspaceMemberRepository.findByWorkspaceId(workspaceId);

        List<WorkspaceMemberEntity> matchedMemberEntities = workspaceMemberEntities.stream().filter(r -> r.getUserId().equals(userId)).collect(Collectors.toList());
        Optional<WorkspaceMemberEntity> workspaceMemberEntityOpt = matchedMemberEntities.stream().findFirst();

        if (!workspaceMemberEntityOpt.isPresent()) {
            return false;
        }

        if (!workspaceMemberEntityOpt.get().getWritePermissionYn().equals("y")) {
            return false;
        }

        return true;
    }

    public boolean isAccessedReadPermissionOfWorkspace(UUID workspaceId, UUID userId) {
        List<WorkspaceMemberEntity> workspaceMemberEntities = workspaceMemberRepository.findByWorkspaceId(workspaceId);

        List<WorkspaceMemberEntity> matchedMemberEntities = workspaceMemberEntities.stream().filter(r -> r.getUserId().equals(userId)).collect(Collectors.toList());
        Optional<WorkspaceMemberEntity> workspaceMemberEntityOpt = matchedMemberEntities.stream().findFirst();

        if (!workspaceMemberEntityOpt.isPresent()) {
            return false;
        }

        if (!workspaceMemberEntityOpt.get().getReadPermissionYn().equals("y")) {
            return false;
        }

        return true;
    }

    public boolean isAccessedUpdatePermissionOfWorkspace(UUID workspaceId, UUID userId) {
        List<WorkspaceMemberEntity> workspaceMemberEntities = workspaceMemberRepository.findByWorkspaceId(workspaceId);

        List<WorkspaceMemberEntity> matchedMemberEntities = workspaceMemberEntities.stream().filter(r -> r.getUserId().equals(userId)).collect(Collectors.toList());
        Optional<WorkspaceMemberEntity> workspaceMemberEntityOpt = matchedMemberEntities.stream().findFirst();

        if (!workspaceMemberEntityOpt.isPresent()) {
            return false;
        }

        if (!workspaceMemberEntityOpt.get().getUpdatePermissionYn().equals("y")) {
            return false;
        }

        return true;
    }

    public boolean isAccessedDeletePermissionOfWorkspace(UUID workspaceId, UUID userId) {
        List<WorkspaceMemberEntity> workspaceMemberEntities = workspaceMemberRepository.findByWorkspaceId(workspaceId);

        List<WorkspaceMemberEntity> matchedMemberEntities = workspaceMemberEntities.stream().filter(r -> r.getUserId().equals(userId)).collect(Collectors.toList());
        Optional<WorkspaceMemberEntity> workspaceMemberEntityOpt = matchedMemberEntities.stream().findFirst();

        if (!workspaceMemberEntityOpt.isPresent()) {
            return false;
        }

        if (!workspaceMemberEntityOpt.get().getDeletePermissionYn().equals("y")) {
            return false;
        }

        return true;
    }

    public List<WorkspaceMemberEntity> searchListByUserId(UUID userId) {
        return workspaceMemberRepository.findByUserId(userId);
    }

    public void deleteByEntity(WorkspaceMemberEntity entity) {
        workspaceMemberRepository.delete(entity);
    }
}
