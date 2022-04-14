package com.sellertool.server.domain.workspace_member.service;

import com.sellertool.server.domain.exception.dto.InvalidUserAuthException;
import com.sellertool.server.domain.exception.dto.NotAllowedAccessException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.user.vo.UserVo;
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

    public Object searchListByWorkspaceId(Object workspaceIdObj) {
        UUID workspaceId = null;
        try{
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        }catch (IllegalArgumentException | NullPointerException e){
            throw new NotMatchedFormatException("요청이 잘못 되었습니다.");
        }

        if(!userService.isLogin()){
            throw new InvalidUserAuthException("토큰이 만료 되었습니다.");
        }

        UUID USER_ID = userService.getUserId();

        List<WorkspaceMemberM2OJProj> proj = workspaceMemberService.searchM2OJProjByWorkspaceId(workspaceId);

        proj.stream().filter(r->r.getWorkspaceMemberEntity().getUserId().equals(USER_ID)).findFirst().orElseThrow(()-> new NotAllowedAccessException("접근 권한이 없습니다."));

        List<WorkspaceMemberM2OJVo> vos = proj.stream().map(WorkspaceMemberM2OJVo::toVo).collect(Collectors.toList());

        return vos;
    }

}
