package com.sellertool.server.domain.product.service;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.category.proj.CategoryM2OJProj;
import com.sellertool.server.domain.category.service.CategoryService;
import com.sellertool.server.domain.product.dto.ProductDto;
import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.vo.ProductVo;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberService;
import com.sellertool.server.utils.CustomUniqueKeyUtils;
import com.sellertool.server.utils.DateTimeUtils;
import com.sellertool.server.utils.FlagInterface;
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
public class ProductBusinessService {
    private final WorkspaceMemberService workspaceMemberService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;

    /**
     * product list search
     */
    /*
    WorkspaceMemberM2OJProj 불러오기
    워크스페이스 존재 여부 확인
    워크스페이스 멤버 여부 확인
    카테고리가 워크스페이스의 소유인지 확인
    search
    Vo transform
     */
    @Transactional(readOnly = true)
    public Object searchList(UUID workspaceId, UUID categoryId){
        UUID USER_ID = userService.getUserId();

        /*
        WorkspaceMemberM2OJProj 불러오기
         */
        WorkspaceMemberM2OJProj workspaceMemberM2OJProj = workspaceMemberService.searchM2OJProjection(workspaceId, USER_ID);

        WorkspaceEntity workspaceEntity = workspaceMemberM2OJProj.getWorkspaceEntity();
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberM2OJProj.getWorkspaceMemberEntity();
        /*
        워크스페이스 존재 여부 확인
         */
        WorkspaceEntity.existence(workspaceEntity);
        /*
        워크스페이스 멤버 여부 확인
         */
        WorkspaceMemberEntity.memberOnlyWE(workspaceMemberEntity);
        /*
        search
         */
        List<ProductEntity> productEntities = productService.searchListByWorkspaceIdAndCategoryId(workspaceId, categoryId);
        List<ProductVo.BasicResponse> vos = productEntities.stream().map(ProductVo.BasicResponse::toVo).collect(Collectors.toList());
        return vos;
    }

    /**
     * product 생성
     */
    /*
    WorkspaceMemberM2OJProj 불러오기
    워크스페이스 존재여부 확인
    워크스페이스 멤버 여부 확인
    워크스페이스 멤버 쓰기 권한 확인
    카테고리가 워크스페이스의 소유인지 확인
    entity set
    save
     */
    @Transactional
    public void createOne(UUID workspaceId, UUID categoryId, ProductDto productDto) {
        UUID USER_ID = userService.getUserId();

        /*
        CategoryM2OJProj 불러오기
         */
        WorkspaceMemberM2OJProj workspaceMemberM2OJProj = workspaceMemberService.searchM2OJProjection(workspaceId, USER_ID);
        CategoryEntity categoryEntity = categoryService.selectById(categoryId);

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

        /*
        워크스페이스 멤버 쓰기 권한 확인
         */
        WorkspaceMemberEntity.hasWritePermission(workspaceMemberEntity);

        /*
        카테고리가 워크스페이스의 소유인지 확인
         */
        CategoryEntity.belongInWorkspace(categoryEntity, workspaceEntity);

        /*
        entity set
         */
        ProductEntity productEntity = ProductEntity.builder()
                .cid(null)
                .id(UUID.randomUUID())
                .code(CustomUniqueKeyUtils.generateKey("g"))
                .defaultName(productDto.getDefaultName())
                .managementName(productDto.getManagementName())
                .imageUrl(productDto.getImageUrl())
                .memo1(productDto.getMemo1())
                .memo2(productDto.getMemo2())
                .memo3(productDto.getMemo3())
                .stockManagementFlag(productDto.isStockManagementFlag())
                .createdAt(DateTimeUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .workspaceCid(workspaceEntity.getCid())
                .workspaceId(workspaceEntity.getId())
                .categoryCid(categoryEntity.getCid())
                .categoryId(categoryEntity.getId())
                .deletedFlag(FlagInterface.SET_EXIST)
                .build();

        /*
        save
         */
        productService.saveAndModify(productEntity);
    }
}
