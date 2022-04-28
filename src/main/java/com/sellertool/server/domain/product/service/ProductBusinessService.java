package com.sellertool.server.domain.product.service;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.category.proj.CategoryM2OJProj;
import com.sellertool.server.domain.category.service.CategoryService;
import com.sellertool.server.domain.product.dto.ProductDto;
import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductBusinessService {
    private final WorkspaceMemberService workspaceMemberService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;

    /**
     * product 생성
     */
    /*
    CategoryM2OJProj 불러오기

     */
    @Transactional
    public void createOne(UUID categoryId, ProductDto productDto) {
        UUID USER_ID = userService.getUserId();

        CategoryM2OJProj categoryM2OJProj = categoryService.searchM2OJProjection(categoryId, USER_ID);

        CategoryEntity categoryEntity = categoryM2OJProj.getCategoryEntity();
        WorkspaceEntity workspaceEntity = categoryM2OJProj.getWorkspaceEntity();
        WorkspaceMemberEntity workspaceMemberEntity = categoryM2OJProj.getWorkspaceMemberEntity();

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

        productService.saveAndModify(productEntity);
    }
}
