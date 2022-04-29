package com.sellertool.server.domain.product.service;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.category.service.CategoryService;
import com.sellertool.server.domain.product.dto.ProductDto;
import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.proj.ProductProjection;
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
    private final ProductInfoService productInfoService;

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
//        List<ProductEntity> productEntities = productService.searchListByWorkspaceIdAndCategoryId(workspaceId, categoryId);
//        List<ProductVo.BasicResponse> vos = productEntities.stream().map(ProductVo.BasicResponse::toVo).collect(Collectors.toList());
//        return vos;
        List<ProductProjection> productProjections = productService.qSearchListByWorkspaceIdAndCategoryId(workspaceId, categoryId);
        List<ProductVo.BasicResponse> vos = productProjections.stream().map(r->ProductVo.BasicResponse.toVo(r.getProductEntity(), r.getProductInfoEntity())).collect(Collectors.toList());
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
    public void createOne(UUID workspaceId, UUID categoryId, ProductDto.CreateRequest productDto) {
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
        UUID productId = UUID.randomUUID();

        ProductEntity.ProductInfoEntity productInfoEntity = ProductEntity.ProductInfoEntity.builder()
                .cid(null)
                .id(productId)
                .memo1(productDto.getMemo1())
                .memo2(productDto.getMemo2())
                .memo3(productDto.getMemo3())
                .build();
        ProductEntity.ProductInfoEntity resProductInfoEntity = productInfoService.saveAndGet(productInfoEntity);

        ProductEntity productEntity = ProductEntity.builder()
                .cid(null)
                .id(productId)
                .code(CustomUniqueKeyUtils.generateKey("g"))
                .defaultName(productDto.getDefaultName())
                .managementName(productDto.getManagementName())
                .imageUrl(productDto.getImageUrl())
                .stockManagementFlag(productDto.isStockManagementFlag())
                .createdAt(DateTimeUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .productInfoCid(resProductInfoEntity.getCid())
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

    /**
     * product 삭제
     * @param workspaceId
     * @param productId
     */
    /*
    WorkspaceMemberM2OJProj 불러오기
    상품 가져오기
    워크스페이스 존재여부 확인
    워크스페이스 멤버 여부 확인
    워크스페이스 멤버 삭제 권한 확인
    상품이 워크스페이스의 소유인지 확인
    delete product
     */
    @Transactional
    public void deleteOne(UUID workspaceId, UUID productId) {
        UUID USER_ID = userService.getUserId();
        /*
        CategoryM2OJProj 불러오기
         */
        WorkspaceMemberM2OJProj workspaceMemberM2OJProj = workspaceMemberService.searchM2OJProjection(workspaceId, USER_ID);
        ProductEntity productEntity = productService.searchById(productId);

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
        워크스페이스 멤버 삭제 권한 확인
         */
        WorkspaceMemberEntity.hasDeletePermission(workspaceMemberEntity);

        /*
        상품이 워크스페이스의 소유인지 확인
         */
        ProductEntity.belongInWorkspace(productEntity, workspaceEntity);

        productService.logicalDeleteOne(productEntity);
    }

    /**
     * product 업데이트
     * @param workspaceId
     * @param productId
     * @param productDto
     */
    /*
    WorkspaceMemberM2OJProj 불러오기
    상품 프로젝션 가져오기
    워크스페이스 존재여부 확인
    워크스페이스 멤버 여부 확인
    워크스페이스 멤버 업데이트 권한 확인
    상품이 워크스페이스의 소유인지 확인
    update product
    update product_info
     */
    @Transactional
    public void updateOne(UUID workspaceId, UUID productId, ProductDto.UpdateRequest productDto) {
        UUID USER_ID = userService.getUserId();

        /*
        WorkspaceMemberM2OJProj 불러오기
         */
        WorkspaceMemberM2OJProj workspaceMemberM2OJProj = workspaceMemberService.searchM2OJProjection(workspaceId, USER_ID);
        ProductProjection productProjection = productService.qSearchM2OJById(productId);

        WorkspaceEntity workspaceEntity = workspaceMemberM2OJProj.getWorkspaceEntity();
        WorkspaceMemberEntity workspaceMemberEntity = workspaceMemberM2OJProj.getWorkspaceMemberEntity();
        ProductEntity productEntity = productProjection.getProductEntity();
        ProductEntity.ProductInfoEntity productInfoEntity = productProjection.getProductInfoEntity();

        /*
        워크스페이스 존재여부 확인
         */
        WorkspaceEntity.existence(workspaceEntity);

        /*
        워크스페이스 멤버 여부 확인
         */
        WorkspaceMemberEntity.memberOnlyWE(workspaceMemberEntity);

        /*
        워크스페이스 멤버 업데이트 권한 확인
         */
        WorkspaceMemberEntity.hasUpdatePermission(workspaceMemberEntity);

        /*
        상품이 워크스페이스의 소유인지 확인
         */
        ProductEntity.belongInWorkspace(productEntity, workspaceEntity);

        /*
        update product
        - Dirty Checking update
         */
        productEntity.setCode(productDto.getCode());
        productEntity.setDefaultName(productDto.getDefaultName());
        productEntity.setManagementName(productDto.getManagementName());
        productEntity.setImageUrl(productDto.getImageUrl());
        productEntity.setStockManagementFlag(productDto.isStockManagementFlag());

        /*
        update product_info
        - Dirty Checking update
         */
        productInfoEntity.setMemo1(productDto.getProductInfo().getMemo1());
        productInfoEntity.setMemo2(productDto.getProductInfo().getMemo2());
        productInfoEntity.setMemo3(productDto.getProductInfo().getMemo3());
    }
}
