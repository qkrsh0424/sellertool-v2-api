package com.sellertool.server.domain.workspace_member.service;

import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace.service.WorkspaceService;
import com.sellertool.server.domain.workspace_member.dto.WorkspaceMemberDto;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import com.sellertool.server.domain.workspace_member.vo.WorkspaceMemberM2OJVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberBusinessService {
    private final UserService userService;
    private final WorkspaceMemberService workspaceMemberService;
    private final WorkspaceService workspaceService;

    /*
    멤버 권한 체크
     */
    public Object searchListByWorkspaceId(UUID workspaceId) {
        UUID USER_ID = userService.getUserId();

        List<WorkspaceMemberM2OJProj> proj = workspaceMemberService.searchM2OJProjectionsByWorkspaceId(workspaceId);

        WorkspaceMemberEntity.memberOnlyWP(proj, USER_ID);

        List<WorkspaceMemberM2OJVo> vos = proj.stream().map(WorkspaceMemberM2OJVo::toVo).collect(Collectors.toList());
        return vos;
    }

    /*
    Get projections
    Find target member's projection
    Get workspace
    Check workspace existence, accessible and master access permission
    If member's userId and login userId same then throw exception
    Delete by entity
     */
    @Transactional
    public void deleteByIdAndWorkspaceId(UUID workspaceMemberId, UUID workspaceId) {
        UUID USER_ID = userService.getUserId();
        /*
        Get projection
         */
        WorkspaceMemberM2OJProj proj = workspaceMemberService.searchM2OJProjection(workspaceMemberId);
        /*
        Get workspace
         */
        WorkspaceEntity workspaceEntity = proj.getWorkspaceEntity();
        /*
        Check workspace existence, accessible and master access permission
         */
        WorkspaceEntity.existence(workspaceEntity);
        WorkspaceEntity.accessible(workspaceEntity, workspaceId);
        WorkspaceEntity.masterOnly(workspaceEntity, USER_ID);

        /*
        If member's userId and login userId same then throw exception
         */
        if (proj.getWorkspaceMemberEntity().getUserId().equals(USER_ID)) {
            throw new NotMatchedFormatException("마스터 권한의 멤버를 제명 할 수 없습니다.");
        }
        /*
        Delete by entity
         */
        workspaceMemberService.deleteByEntity(proj.getWorkspaceMemberEntity());
    }

    /**
     * 워크스페이스 멤버의 권한들을 업데이트 한다.
     * writePermissionYn, updatePermissionYn, deletePermissionYn
     *
     * @param workspaceMemberId
     * @param workspaceMemberDto
     */
    /*
    WorkspaceMemberM2OJProj 불러오기
    Get workspace and workspace member entity
    워크스페이스 및 마스터 권한 검사
    writePermissionYn, updatePermissionYn, deletePermissionYn 업데이트
     */
    @Transactional
    public void changePermissions(UUID workspaceMemberId, WorkspaceMemberDto workspaceMemberDto) {
        UUID USER_ID = userService.getUserId();
        /*
        WorkspaceMemberM2OJProj 불러오기
         */
        WorkspaceMemberM2OJProj proj = workspaceMemberService.searchM2OJProjection(workspaceMemberId);
        /*
        Get workspace and workspace member entity
         */
        WorkspaceEntity workspaceEntity = proj.getWorkspaceEntity();
        WorkspaceMemberEntity workspaceMemberEntity = proj.getWorkspaceMemberEntity();
        /*
        워크스페이스 및 마스터 권한 검사
         */
        WorkspaceEntity.existence(workspaceEntity);
        WorkspaceEntity.masterOnly(workspaceEntity, USER_ID);

        /*
        writePermissionYn, updatePermissionYn, deletePermissionYn 업데이트
        Dirty Checking update
         */
        workspaceMemberEntity.setWritePermissionYn(workspaceMemberDto.getWritePermissionYn());
        workspaceMemberEntity.setUpdatePermissionYn(workspaceMemberDto.getUpdatePermissionYn());
        workspaceMemberEntity.setDeletePermissionYn(workspaceMemberDto.getDeletePermissionYn());
    }
}
