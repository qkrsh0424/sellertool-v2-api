package com.sellertool.server.domain.workspace_member.service;

import com.sellertool.server.annotation.WorkspacePermission;
import com.sellertool.server.domain.exception.dto.InvalidUserAuthException;
import com.sellertool.server.domain.exception.dto.NotAllowedAccessException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.user.vo.UserVo;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace.vo.WorkspaceVo;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import com.sellertool.server.domain.workspace_member.vo.WorkspaceMemberM2OJVo;
import com.sellertool.server.domain.workspace_member.vo.WorkspaceMemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceMemberBusinessService {
    private final UserService userService;
    private final WorkspaceMemberService workspaceMemberService;

    /*
    멤버 권한 체크
     */
    public Object searchListByWorkspaceId(UUID workspaceId) {
        UUID USER_ID = userService.getUserId();

        List<WorkspaceMemberM2OJProj> proj = workspaceMemberService.searchM2OJProjByWorkspaceId(workspaceId);

        WorkspaceMemberEntity.memberOnlyWP(proj, USER_ID);

        List<WorkspaceMemberM2OJVo> vos = proj.stream().map(WorkspaceMemberM2OJVo::toVo).collect(Collectors.toList());
        return vos;
    }

}
