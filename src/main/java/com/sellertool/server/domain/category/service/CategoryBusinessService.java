package com.sellertool.server.domain.category.service;

import com.sellertool.server.domain.category.dto.CategoryDto;
import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.category.vo.CategoryVo;
import com.sellertool.server.domain.exception.dto.NotAllowedAccessException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberService;
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
public class CategoryBusinessService {
    private final CategoryService categoryService;
    private final WorkspaceMemberService workspaceMemberService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Object searchList(UUID workspaceId) {
        UUID USER_ID = userService.getUserId();

        WorkspaceMemberM2OJProj workspaceMemberM2OJProj = workspaceMemberService.searchM2OJProjection(workspaceId, USER_ID);

        WorkspaceEntity workspaceEntity = workspaceMemberM2OJProj.getWorkspaceEntity();
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberM2OJProj.getWorkspaceMemberEntity();

        /*
        워크스페이스 존재여부 확인
         */
        WorkspaceEntity.existence(workspaceEntity);

        /*
        워크스페이스 멤버 여부 확인
         */
        WorkspaceMemberEntity.memberOnlyWE(workspaceMemberEntity);

        List<CategoryEntity> categoryEntities = categoryService.selectListByWorkspaceId(workspaceId);

        List<CategoryVo.BasicResponse> categoryVo = categoryEntities.stream().map(CategoryVo.BasicResponse::toVo).collect(Collectors.toList());
        return categoryVo;
    }

    /**
     * 카테고리 생성 로직
     *
     * @param workspaceId
     * @param categoryDto
     */
    /*
    로그인 유저 아이디와 워크스페이스 아이디를 이용해 WorkspaceMemberM2OJProj 불러오기
    워크스페이스 존재여부 확인
    워크스페이스 멤버 쓰기 권한 확인
    데이터 저장
     */
    @Transactional
    public void createOne(UUID workspaceId, CategoryDto categoryDto) {
        UUID USER_ID = userService.getUserId();
        WorkspaceMemberM2OJProj workspaceMemberM2OJProj = workspaceMemberService.searchM2OJProjection(workspaceId, USER_ID);

        WorkspaceEntity workspaceEntity = workspaceMemberM2OJProj.getWorkspaceEntity();
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberM2OJProj.getWorkspaceMemberEntity();

        /*
        워크스페이스 존재여부 확인
         */
        WorkspaceEntity.existence(workspaceEntity);

        /*
        워크스페이스 멤버 쓰기 권한 확인
         */
        WorkspaceMemberEntity.hasWritePermission(workspaceMemberEntity);

        CategoryEntity categoryEntity = CategoryEntity.builder()
                .cid(null)
                .id(UUID.randomUUID())
                .name(categoryDto.getName())
                .createdAt(DateTimeUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .workspaceCid(workspaceEntity.getCid())
                .workspaceId(workspaceEntity.getId())
                .build();

        /*
        데이터 저장
         */
        categoryService.saveAndModify(categoryEntity);
    }


    /**
     * 카테고리 수정 로직
     *
     * @param workspaceId
     * @param categoryDto
     */
    /*
    로그인 유저 아이디와 워크스페이스 아이디를 이용해 WorkspaceMemberM2OJProj 불러오기
    워크스페이스 존재여부 확인
    워크스페이스 멤버 업데이트 권한 확인
    파라메터의 워크스페이스 아이디와 카테고리의 워크스페이스 아이디를 비교
    업데이트
     */
    @Transactional
    public void updateOne(UUID workspaceId, CategoryDto categoryDto) {
        UUID USER_ID = userService.getUserId();
        /*
        로그인 유저 아이디와 워크스페이스 아이디를 이용해 WorkspaceMemberM2OJProj 불러오기
         */
        WorkspaceMemberM2OJProj workspaceMemberM2OJProj = workspaceMemberService.searchM2OJProjection(workspaceId, USER_ID);

        WorkspaceEntity workspaceEntity = workspaceMemberM2OJProj.getWorkspaceEntity();
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberM2OJProj.getWorkspaceMemberEntity();

        /*
        워크스페이스 존재여부 확인
         */
        WorkspaceEntity.existence(workspaceEntity);

        /*
        워크스페이스 멤버 업데이트 권한 확인
         */
        WorkspaceMemberEntity.hasUpdatePermission(workspaceMemberEntity);
        CategoryEntity categoryEntity = categoryService.selectById(categoryDto.getId());

        /*
        파라메터의 워크스페이스 아이디와 카테고리의 워크스페이스 아이디를 비교
         */
        if (!categoryEntity.getWorkspaceId().equals(workspaceId)) {
            throw new NotAllowedAccessException("해당 데이터에 접근 권한이 없습니다.");
        }

        /*
        Dirty Checking update
         */
        categoryEntity.setName(categoryDto.getName());

    }

    /**
     * 카테고리를 삭제하는 로직
     * @param workspaceId
     * @param categoryId
     */
    /*
    로그인 유저 아이디와 워크스페이스 아이디를 이용해 WorkspaceMemberM2OJProj 불러오기
    워크스페이스 존재여부 확인
    워크스페이스 멤버 삭제 권한 확인
    카테고리 불러오기
    파라메터의 워크스페이스 아이디와 카테고리의 워크스페이스 아이디를 비교
    삭제
     */
    @Transactional
    public void deleteOne(UUID workspaceId, UUID categoryId) {
        UUID USER_ID = userService.getUserId();
        /*
        로그인 유저 아이디와 워크스페이스 아이디를 이용해 WorkspaceMemberM2OJProj 불러오기
         */
        WorkspaceMemberM2OJProj workspaceMemberM2OJProj = workspaceMemberService.searchM2OJProjection(workspaceId, USER_ID);

        WorkspaceEntity workspaceEntity = workspaceMemberM2OJProj.getWorkspaceEntity();
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberM2OJProj.getWorkspaceMemberEntity();

        /*
        워크스페이스 존재여부 확인
         */
        WorkspaceEntity.existence(workspaceEntity);

        /*
        워크스페이스 멤버 삭제 권한 확인
         */
        WorkspaceMemberEntity.hasDeletePermission(workspaceMemberEntity);

        CategoryEntity categoryEntity = categoryService.selectById(categoryId);
        /*
        파라메터의 워크스페이스 아이디와 카테고리의 워크스페이스 아이디를 비교
         */
        if (!categoryEntity.getWorkspaceId().equals(workspaceId)) {
            throw new NotAllowedAccessException("해당 데이터에 접근 권한이 없습니다.");
        }

        categoryService.logicalDeleteOne(categoryEntity);
    }
}
