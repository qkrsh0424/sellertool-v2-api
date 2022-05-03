package com.sellertool.server.domain.option.service;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.option.dto.OptionDto;
import com.sellertool.server.domain.option.entity.OptionEntity;
import com.sellertool.server.domain.option.proj.OptionProjection;
import com.sellertool.server.domain.option.vo.OptionVo;
import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.proj.ProductProjection;
import com.sellertool.server.domain.product.service.ProductService;
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
public class OptionBusinessService {
    private final OptionService optionService;
    private final OptionInfoService optionInfoService;
    private final ProductService productService;
    private final UserService userService;
    private final WorkspaceMemberService workspaceMemberService;


    /**
     * option list search
     */
    /*
    WorkspaceMemberM2OJProj 불러오기
    워크스페이스 존재 여부 확인
    워크스페이스 멤버 여부 확인
    상품이 워크스페이스의 소유인지 확인
    search
    Vo transform
     */
    @Transactional(readOnly = true)
    public Object searchList(UUID workspaceId, UUID productId){
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
        List<OptionProjection> optionProjections = optionService.qSearchListByWorkspaceIdAndProductId(workspaceId, productId);
        List<OptionVo.BasicResponse> vos = optionProjections.stream().map(r->OptionVo.BasicResponse.toVo(r.getOptionEntity(), r.getOptionInfoEntity())).collect(Collectors.toList());
        return vos;
    }

    /**
     * option 생성
     */
    /*
    WorkspaceMemberM2OJProj 불러오기
    product 불러오기
    워크스페이스 존재여부 확인
    워크스페이스 멤버 여부 확인
    워크스페이스 멤버 쓰기 권한 확인
    product가 워크스페이스의 소유인지 확인
    entity set
    save
     */
    @Transactional
    public void createOne(UUID workspaceId, UUID productId, OptionDto.CreateRequest optionDto) {
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
        워크스페이스 멤버 쓰기 권한 확인
         */
        WorkspaceMemberEntity.hasWritePermission(workspaceMemberEntity);

        /*
        product가 워크스페이스의 소유인지 확인
         */
        ProductEntity.belongInWorkspace(productEntity, workspaceEntity);

        UUID optionId = UUID.randomUUID();
        OptionEntity.OptionInfoEntity optionInfoEntity = OptionEntity.OptionInfoEntity.builder()
                .cid(null)
                .id(optionId)
                .status(optionDto.getOptionInfo().getStatus())
                .salesPrice(optionDto.getOptionInfo().getSalesPrice())
                .memo1(optionDto.getOptionInfo().getMemo1())
                .memo2(optionDto.getOptionInfo().getMemo2())
                .memo3(optionDto.getOptionInfo().getMemo3())
                .build();

        OptionEntity.OptionInfoEntity resOptionInfoEntity = optionInfoService.saveAndGet(optionInfoEntity);

        OptionEntity optionEntity = OptionEntity.builder()
                .cid(null)
                .id(optionId)
                .code(CustomUniqueKeyUtils.generateKey("o"))
                .defaultName(optionDto.getDefaultName())
                .managementName(optionDto.getManagementName())
                .imageUrl(optionDto.getImageUrl())
                .stockUnit(FlagInterface.SET_STOCK_UNIT_INIT)
                .createdAt(DateTimeUtils.getCurrentDateTime())
                .createdBy(USER_ID)
                .optionInfoCid(resOptionInfoEntity.getCid())
                .productCid(productEntity.getCid())
                .productId(productEntity.getId())
                .workspaceCid(workspaceEntity.getCid())
                .workspaceId(workspaceEntity.getId())
                .deletedFlag(FlagInterface.SET_EXIST)
                .build();
        optionService.saveAndModify(optionEntity);
    }

    /**
     * option 삭제
     */
    /*
    WorkspaceMemberM2OJProj 불러오기
    워크스페이스 존재 여부 확인
    워크스페이스 멤버 여부 확인
    워크스페이스 삭제 권한 확인
    optionEntity 불러오기
    option 이 워크스페이스 소유 인지 확인
    삭제
     */
    @Transactional
    public void deleteOne(UUID workspaceId, UUID optionId) {
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
        워크스페이스 삭제 권한 확인
         */
        WorkspaceMemberEntity.hasDeletePermission(workspaceMemberEntity);
        /*
        option entity 불러오기
         */
        OptionEntity optionEntity = optionService.searchById(optionId);
        /*
        option이 워크스페이스 소유 인지 확인
         */
        OptionEntity.belongInWorkspace(optionEntity, workspaceEntity);
        /*
        삭제 Logical delete
         */
        optionService.logicalDeleteOne(optionEntity);
    }
}
