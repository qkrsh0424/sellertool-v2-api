package com.sellertool.server.domain.workspace.service;

import com.sellertool.server.annotation.WorkspacePermission;
import com.sellertool.server.domain.exception.dto.InvalidUserAuthException;
import com.sellertool.server.domain.exception.dto.NotAllowedAccessException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.workspace.dto.WorkspaceDto;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace.utils.WorkspaceStaticVariable;
import com.sellertool.server.domain.workspace.vo.WorkspaceVo;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberService;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace_member.utils.WorkspaceMemberStaticVariable;
import com.sellertool.server.utils.DateTimeUtils;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceBusinessService {
    private final WorkspaceService workspaceService;
    private final WorkspaceMemberService workspaceMemberService;
    private final UserService userService;

    @Autowired
    public WorkspaceBusinessService(
            WorkspaceService workspaceService,
            WorkspaceMemberService workspaceMemberService,
            UserService userService
    ) {
        this.workspaceService = workspaceService;
        this.workspaceMemberService = workspaceMemberService;
        this.userService = userService;
    }

    public Object searchWorkspace(Object workspaceIdObj) {
        UUID USER_ID = userService.getUserId();
        UUID workspaceId = null;

        if (workspaceIdObj == null) {
            return WorkspaceVo.toVo(workspaceService.searchOwnPrivateWorkspace(USER_ID));
        }

        try {
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        } catch (IllegalArgumentException e) {
            return WorkspaceVo.toVo(workspaceService.searchOwnPrivateWorkspace(USER_ID));
        }

        WorkspaceEntity workspaceEntity = workspaceService.searchWorkspaceOne(workspaceId);

        List<WorkspaceMemberEntity> workspaceMemberEntities = workspaceMemberService.searchListByWorkspaceId(workspaceId);
        List<UUID> memberIds = workspaceMemberEntities.stream().map(r -> r.getUserId()).collect(Collectors.toList());

        if (memberIds.contains(USER_ID)) {
            return WorkspaceVo.toVo(workspaceEntity);
        }

        return WorkspaceVo.toVo(workspaceService.searchOwnPrivateWorkspace(USER_ID));
    }

    public Object searchListByUser() {
        UUID USER_ID = userService.getUserId();

        List<WorkspaceEntity> workspaceEntities = workspaceService.searchListByUserId(USER_ID);
        List<WorkspaceVo> workspaceVos = workspaceEntities.stream().map(r -> {
            return WorkspaceVo.toVo(r);
        }).collect(Collectors.toList());

        return workspaceVos;
    }

    @Transactional
    public Object createPrivate(WorkspaceDto workspaceDto) {
        UUID USER_ID = userService.getUserId();
        UUID WORKSPACE_ID = UUID.randomUUID();
        UUID WORKSPACE_MEMBER_ID = UUID.randomUUID();

        WorkspaceEntity workspaceEntity = WorkspaceEntity.builder()
                .id(WORKSPACE_ID)
                .name(workspaceDto.getName())
                .masterId(USER_ID)
                .deleteProtectionYn(WorkspaceStaticVariable.DELETE_PROTECTION_N)
                .publicYn(WorkspaceStaticVariable.PUBLIC_N)
                .createdAt(DateTimeUtils.getCurrentDateTime())
                .updatedAt(DateTimeUtils.getCurrentDateTime())
                .build();

        WorkspaceMemberEntity workspaceMemberEntity = WorkspaceMemberEntity.builder()
                .id(WORKSPACE_MEMBER_ID)
                .workspaceId(WORKSPACE_ID)
                .userId(USER_ID)
                .grade(WorkspaceMemberStaticVariable.GRADE_MASTER)
                .createdAt(DateTimeUtils.getCurrentDateTime())
                .readPermissionYn(WorkspaceMemberStaticVariable.READ_PERMISSION_Y)
                .writePermissionYn(WorkspaceMemberStaticVariable.WRITE_PERMISSION_Y)
                .updatePermissionYn(WorkspaceMemberStaticVariable.UPDATE_PERMISSION_Y)
                .deletePermissionYn(WorkspaceMemberStaticVariable.DELETE_PERMISSION_Y)
                .build();

        workspaceService.saveAndModify(workspaceEntity);
        workspaceMemberService.saveAndModify(workspaceMemberEntity);

        return WORKSPACE_ID;
    }

    /*
    로그인 체크
    워크스페이스 존재여부 검사
    워크스페이스 마스터 권한 검사
     */
    @Transactional
    public void changeNameByWorkspaceId(UUID workspaceId, Map<String, Object> body) {
        WorkspaceEntity workspaceEntity = workspaceService.searchWorkspaceOne(workspaceId);
        UUID USER_ID = userService.getUserId();

        WorkspaceEntity.existence(workspaceEntity);
        WorkspaceEntity.masterOnly(workspaceEntity, USER_ID);

        String newName = body.get("name").toString();

        workspaceEntity.setName(newName);
    }
}
