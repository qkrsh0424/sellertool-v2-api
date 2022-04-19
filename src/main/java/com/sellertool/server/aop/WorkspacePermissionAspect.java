package com.sellertool.server.aop;

import com.sellertool.server.annotation.WorkspacePermission;
import com.sellertool.server.config.auth.PrincipalDetails;
import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.exception.dto.InvalidUserAuthException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace.repository.WorkspaceRepository;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class WorkspacePermissionAspect {
    private final UserService userService;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    /*
    워크스페이스 존재 여부 체크
     */
    @Before(value="@annotation(workspacePermission)")
    public void workspacePermission(JoinPoint joinPoint, WorkspacePermission workspacePermission) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        /**
         * 메서드로 들어온 파라메터와 값들을 가져온다.
         */
        List<Object> args = Arrays.stream(joinPoint.getArgs()).collect(Collectors.toList());
        List<Parameter> parameters = Arrays.stream(signature.getMethod().getParameters()).collect(Collectors.toList());

        UUID workspaceId = null;

        /**
         * 파라메터중 workspaceId 로 선언된 데이터를 찾아서 매핑시킴
         */
        for(int i =0; i< parameters.size(); i++){
            if(parameters.get(i).getName().equals("workspaceId")){
                workspaceId = UUID.fromString(args.get(i).toString());
            }
        }

        /**
         * 워크스페이스 존재 여부 확인
         */

        WorkspaceEntity workspaceEntity = workspaceRepository.findById(workspaceId).orElseThrow(()-> new NotMatchedFormatException("워크스페이스를 찾을 수 없음."));


        UUID userId = userService.getUserId();

        /**
         * 워크스페이스 마스터 권한 체크
         * WorkspacePermission 어노테이션의 MasterOnly 필드가 true 일때 작동
         */
        if(workspacePermission.MasterOnly()){

            if(!workspaceEntity.getMasterId().equals(userId)){
                throw new AccessDeniedPermissionException("워크스페이스 마스터에 접근 권한이 없습니다.");
            }
        }

        /**
         * 워크스페이스 멤버 권한 체크
         * WorkspacePermission 어노테이션의 MasterOnly 필드가 true 일때 작동
         */
        if(workspacePermission.MemberOnly()){
            List<WorkspaceMemberEntity> workspaceMemberEntities = workspaceMemberRepository.findByWorkspaceId(workspaceId);
            workspaceMemberEntities.stream().filter(r->r.getUserId().equals(userId)).findFirst().orElseThrow(()-> new AccessDeniedPermissionException("워크스페이스 멤버에 접근 권한이 없습니다."));
        }

        /**
         * 워크스페이스 퍼블릭 체크
         * WorkspacePermission 어노테이션의 PublicOnly 필드가 true 일때 작동
         */
        if(workspacePermission.PublicOnly()){
            if(!workspaceEntity.getPublicYn().equals("y")){
                throw new AccessDeniedPermissionException("워크스페이스가 퍼블릭 상태가 아닙니다.");
            }
        }

    }
}
