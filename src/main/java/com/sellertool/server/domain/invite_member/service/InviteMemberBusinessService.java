package com.sellertool.server.domain.invite_member.service;

import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.invite_member.dto.InviteMemberDto;
import com.sellertool.server.domain.invite_member.entity.InviteMemberEntity;
import com.sellertool.server.domain.invite_member.proj.InviteMemberM2OJProj;
import com.sellertool.server.domain.invite_member.vo.InviteMemberM2OJVo;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace.service.WorkspaceService;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberService;
import com.sellertool.server.domain.workspace_member.utils.WorkspaceMemberStaticVariable;
import com.sellertool.server.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InviteMemberBusinessService {
    private final UserService userService;
    private final WorkspaceService workspaceService;
    private final WorkspaceMemberService workspaceMemberService;
    private final InviteMemberService inviteMemberService;

    /**
     * 워크스페이스 존재 여부 확인
     * 워크스페이스 퍼블릭 체크
     * 워크스페이스 마스터 권한 체크
     * 초대 대상이 플랫폼 회원인지 체크
     * 초대 대상이 이미 워크스페이스 멤버인지 체크
     * 초대 대상이 이미 멤버 초대 리스트에 있는지 체크
     * 초대 리스트에 데이터 삽입
     *
     * @param inviteMemberDto
     */
    @Transactional
    public void createOne(
            UUID workspaceId,
            InviteMemberDto inviteMemberDto
    ) {
        UUID USER_ID = userService.getUserId();
        UserEntity targetUserEntity = userService.searchByUsername(inviteMemberDto.getUsername());
        WorkspaceEntity workspaceEntity = workspaceService.searchWorkspaceOne(workspaceId);

        WorkspaceEntity.existence(workspaceEntity);
        WorkspaceEntity.masterOnly(workspaceEntity, USER_ID);
        WorkspaceEntity.publicOnly(workspaceEntity);

//        초대 대상이 플랫폼 회원인지 체크
        if (targetUserEntity == null) {
            throw new NotMatchedFormatException("초대 대상 회원을 찾을 수 없습니다.");
        }

//        초대 대상이 이미 워크스페이스 멤버인지 체크
        List<WorkspaceMemberEntity> workspaceMemberEntities = workspaceMemberService.searchListByWorkspaceId(workspaceEntity.getId());
        workspaceMemberEntities.forEach(r -> {
            if (r.getUserId().equals(targetUserEntity.getId())) {
                throw new NotMatchedFormatException("이미 워크스페이스 멤버 입니다.");
            }
        });

//        초대 대상이 이미 멤버 초대 리스트에 있는지 체크
        List<InviteMemberEntity> inviteMemberEntities = inviteMemberService.searchByWorkspaceId(workspaceEntity.getId());
        inviteMemberEntities.forEach(r -> {
            if (r.getUserId().equals(targetUserEntity.getId())) {
                throw new NotMatchedFormatException("이미 초대된 회원입니다.");
            }
        });

        InviteMemberEntity inviteMemberEntity = InviteMemberEntity.builder()
                .cid(null)
                .id(UUID.randomUUID())
                .userId(targetUserEntity.getId())
                .workspaceId(workspaceEntity.getId())
                .build();
        inviteMemberService.saveAndModify(inviteMemberEntity);
    }

    /*
    워크스페이스 존재여부 확인
    워크스페이스 마스터 권한 확인
     */
    @Transactional(readOnly = true)
    public Object searchM2OJByWorkspaceId(UUID workspaceId) {
        UUID USER_ID = userService.getUserId();
        WorkspaceEntity workspaceEntity = workspaceService.searchWorkspaceOne(workspaceId);

        WorkspaceEntity.existence(workspaceEntity);
        WorkspaceEntity.masterOnly(workspaceEntity, USER_ID);

        List<InviteMemberM2OJProj> proj = inviteMemberService.qSelectM2OJByWorkspaceId(workspaceId);
        List<InviteMemberM2OJVo> vos = proj.stream().map(InviteMemberM2OJVo::toVo).collect(Collectors.toList());
        return vos;
    }

    /*
        유저 아이디 GET
        유저 아이디를 이용한 M2OJ 서치
     */
    @Transactional(readOnly = true)
    public Object searchM2OJByRequested() {
        UUID USER_ID = userService.getUserId();

        List<InviteMemberM2OJProj> proj = inviteMemberService.qSelectM2OJByUserId(USER_ID);
        List<InviteMemberM2OJVo> vos = proj.stream().map(InviteMemberM2OJVo::toVo).collect(Collectors.toList());
        return vos;
    }

    /*
     워크스페이스 존재 여부 확인
     워크스페이스 마스터 권한 체크
     */
    @Transactional
    public void deleteByWorkspaceIdAndInviteMemberId(UUID workspaceId, UUID inviteMemberId) {
        UUID USER_ID = userService.getUserId();
        WorkspaceEntity workspaceEntity = workspaceService.searchWorkspaceOne(workspaceId);

        WorkspaceEntity.existence(workspaceEntity);
        WorkspaceEntity.masterOnly(workspaceEntity, USER_ID);

        inviteMemberService.deleteByWorkspaceIdAndInviteMemberId(workspaceId, inviteMemberId);
    }

    /*
    inviteMemberProj 데이터 가져오기
    inviteMember의 유저 아이디와 계정 유저 아이디 검증
    workspaceMemberEntities 조회
    workspaceMemberEntities 내부에 해당 유저 아이디와 같은 멤버가 있다면 에러
    workspaceMemberEntity 생성
    workspaceMember 저장
    inviteMember 삭제
     */
    @Transactional
    public void acceptMemberInWorkspace(UUID inviteMemberId) {
        /*
        inviteMemberProj 데이터 가져오기
         */
        InviteMemberM2OJProj proj = inviteMemberService.qSelectM2OJById(inviteMemberId);
        InviteMemberEntity inviteMemberEntity = proj.getInviteMemberEntity();
        UserEntity userEntity = proj.getUserEntity();
        WorkspaceEntity workspaceEntity = proj.getWorkspaceEntity();

        if (proj == null) {
            throw new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다.");
        }

        UUID USER_ID = userService.getUserId();

        /*
        inviteMember의 유저 아이디와 계정 유저 아이디 검증
         */
        if (!userEntity.getId().equals(USER_ID)) {
            throw new NotMatchedFormatException("정상적인 요청이 아닙니다.");
        }

        /*
        workspaceMemberEntities 조회
        workspaceMemberEntities 내부에 해당 유저 아이디와 같은 멤버가 있다면 에러
         */
        List<WorkspaceMemberEntity> workspaceMemberEntities = workspaceMemberService.searchListByWorkspaceId(workspaceEntity.getId());
        workspaceMemberEntities.stream().filter(r->r.getUserId().equals(USER_ID)).findAny().orElseThrow(() -> new NotMatchedFormatException("정상적인 요청이 아닙니다."));

        /*
        workspaceMemberEntity 생성
         */
        WorkspaceMemberEntity workspaceMemberEntity = WorkspaceMemberEntity
                .builder()
                .id(UUID.randomUUID())
                .workspaceId(workspaceEntity.getId())
                .userId(USER_ID)
                .grade(WorkspaceMemberStaticVariable.GRADE_MEMBER)
                .createdAt(DateTimeUtils.getCurrentDateTime())
                .readPermissionYn(WorkspaceMemberStaticVariable.READ_PERMISSION_Y)
                .writePermissionYn(WorkspaceMemberStaticVariable.WRITE_PERMISSION_N)
                .updatePermissionYn(WorkspaceMemberStaticVariable.UPDATE_PERMISSION_N)
                .deletePermissionYn(WorkspaceMemberStaticVariable.DELETE_PERMISSION_N)
                .build();

        /*
        workspaceMember 저장
        inviteMember 삭제
         */
        workspaceMemberService.saveAndModify(workspaceMemberEntity);
        inviteMemberService.deleteByEntity(inviteMemberEntity);
    }

    /*
    inviteMemberProj 데이터 가져오기
    inviteMember의 유저 아이디와 계정 유저 아이디 검증
    inviteMember 삭제
     */
    public void rejectMemberInWorkspace(UUID inviteMemberId) {
        /*
        inviteMemberProj 데이터 가져오기
         */
        InviteMemberM2OJProj proj = inviteMemberService.qSelectM2OJById(inviteMemberId);
        InviteMemberEntity inviteMemberEntity = proj.getInviteMemberEntity();
        UserEntity userEntity = proj.getUserEntity();

        if (proj == null) {
            throw new NotMatchedFormatException("요청 데이터를 찾을 수 없습니다.");
        }

        /*
        inviteMember의 유저 아이디와 계정 유저 아이디 검증
         */
        UUID USER_ID = userService.getUserId();
        if (!userEntity.getId().equals(USER_ID)) {
            throw new NotMatchedFormatException("잘못된 요청 입니다.");
        }

        /*
        inviteMember 삭제
         */
        inviteMemberService.deleteByEntity(inviteMemberEntity);
    }
}
