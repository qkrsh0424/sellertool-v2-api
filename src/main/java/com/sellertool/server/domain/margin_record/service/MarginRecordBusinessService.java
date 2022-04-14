package com.sellertool.server.domain.margin_record.service;

import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.exception.dto.InvalidUserAuthException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.margin_record.dto.MarginRecordDto;
import com.sellertool.server.domain.margin_record.entity.MarginRecordEntity;
import com.sellertool.server.domain.margin_record.vo.MarginRecordVo;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberService;
import com.sellertool.server.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MarginRecordBusinessService {
    private final UserService userService;
    private final WorkspaceMemberService workspaceMemberService;
    private final MarginRecordService marginRecordService;

    @Autowired
    public MarginRecordBusinessService(
            UserService userService,
            WorkspaceMemberService workspaceMemberService,
            MarginRecordService marginRecordService
    ) {
        this.userService = userService;
        this.workspaceMemberService = workspaceMemberService;
        this.marginRecordService = marginRecordService;
    }

    public MarginRecordVo createOneAndGet(MarginRecordDto marginRecordDto) {
        if (!userService.isLogin()) {
            throw new InvalidUserAuthException("로그인이 필요한 서비스 입니다.");
        }

        UUID USER_ID = userService.getUserId();
        UUID MARGIN_RECORD_ID = UUID.randomUUID();
        UUID WORKSPACE_ID = marginRecordDto.getWorkspaceId();
        UUID OPEN_KEY = UUID.randomUUID();

        if (WORKSPACE_ID == null) {
            throw new NotMatchedFormatException("워크스페이스를 지정해주세요.");
        }

        if (!workspaceMemberService.isAccessedWritePermissionOfWorkspace(WORKSPACE_ID, USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        marginRecordDto.setId(MARGIN_RECORD_ID);
        marginRecordDto.setOpenKey(OPEN_KEY);
        marginRecordDto.setWorkspaceId(WORKSPACE_ID);
        marginRecordDto.setCreatedBy(USER_ID);
        marginRecordDto.setCreatedAt(DateTimeUtils.getCurrentDateTime());
        marginRecordDto.setUpdatedAt(DateTimeUtils.getCurrentDateTime());

        MarginRecordEntity marginRecordEntity = MarginRecordEntity.toEntity(marginRecordDto);

        try {
            MarginRecordEntity resultEntity = marginRecordService.saveAndGet(marginRecordEntity);
            return MarginRecordVo.toVo(resultEntity);
        } catch (Exception e) {
            throw new NotMatchedFormatException("양식을 다시 확인해 주세요.");
        }

    }

    public void updateOne(MarginRecordDto marginRecordDto) {
        if (!userService.isLogin()) {
            throw new InvalidUserAuthException("로그인이 필요한 서비스 입니다.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("marginRecordId", marginRecordDto.getId());

        UUID USER_ID = userService.getUserId();
        MarginRecordEntity marginRecordEntity = marginRecordService.searchList(params).stream().findFirst().orElseThrow(()-> new NotMatchedFormatException("요청하신 정보를 찾을 수 없습니다."));
        UUID WORKSPACE_ID = marginRecordEntity.getWorkspaceId();

        if (!workspaceMemberService.isAccessedUpdatePermissionOfWorkspace(WORKSPACE_ID, USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        marginRecordEntity.setName(marginRecordDto.getName());
        marginRecordEntity.setSalePrice(marginRecordDto.getSalePrice());
        marginRecordEntity.setPurchaseCost(marginRecordDto.getPurchaseCost());
        marginRecordEntity.setConsumerDeliveryCharge(marginRecordDto.getConsumerDeliveryCharge());
        marginRecordEntity.setSellerDeliveryCharge(marginRecordDto.getSellerDeliveryCharge());
        marginRecordEntity.setPurchaseDeliveryCharge(marginRecordDto.getPurchaseDeliveryCharge());
        marginRecordEntity.setExtraCost(marginRecordDto.getExtraCost());
        marginRecordEntity.setCommission(marginRecordDto.getCommission());
        marginRecordEntity.setTotalIncome(marginRecordDto.getTotalIncome());
        marginRecordEntity.setTotalIncomeInterestExpense(marginRecordDto.getTotalIncomeInterestExpense());
        marginRecordEntity.setTotalExpense(marginRecordDto.getTotalExpense());
        marginRecordEntity.setMargin(marginRecordDto.getMargin());
        marginRecordEntity.setMarginRate(marginRecordDto.getMarginRate());
        marginRecordEntity.setIncomeTax(marginRecordDto.getIncomeTax());
        marginRecordEntity.setExpenseTax(marginRecordDto.getExpenseTax());
        marginRecordEntity.setTotalTax(marginRecordDto.getTotalTax());

        marginRecordService.saveAndModify(marginRecordEntity);
    }

    public Object searchOne(Map<String, Object> params) {
        if (!userService.isLogin()) {
            throw new InvalidUserAuthException("로그인이 필요한 서비스 입니다.");
        }

        UUID USER_ID = userService.getUserId();

        if (params.get("marginRecordId") == null) {
            throw new NotMatchedFormatException("정상적인 요청이 아닙니다.");
        }

        MarginRecordEntity marginRecordEntity = marginRecordService.searchList(params).stream().findFirst().orElseThrow(()-> new NotMatchedFormatException("요청하신 정보를 찾을 수 없습니다."));

        if (!workspaceMemberService.isAccessedReadPermissionOfWorkspace(marginRecordEntity.getWorkspaceId(), USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        return MarginRecordVo.toVo(marginRecordEntity);
    }

    public Object searchViewerOne(Map<String, Object> params) {
        Object marginRecordIdObj = params.get("marginRecordId");
        Object marginRecordOpenKey = params.get("openKey");

        UUID MARGIN_RECORD_ID = null;
        UUID MARGIN_RECORD_OPEN_KEY = null;

        if(marginRecordIdObj == null || marginRecordOpenKey == null){
            throw new NotMatchedFormatException("요청하신 정보를 찾을 수 없습니다.");
        }

        try{
            MARGIN_RECORD_ID = UUID.fromString(marginRecordIdObj.toString());
            MARGIN_RECORD_OPEN_KEY = UUID.fromString(marginRecordOpenKey.toString());
        } catch (IllegalArgumentException e){
            throw new NotMatchedFormatException("요청하신 정보를 찾을 수 없습니다.");
        }

        MarginRecordEntity marginRecordEntity = marginRecordService.searchList(params).stream().findFirst().orElseThrow(()->new NotMatchedFormatException("요청하신 정보를 찾을 수 없습니다."));

        if(!MARGIN_RECORD_OPEN_KEY.equals(marginRecordEntity.getOpenKey())){
            throw new NotMatchedFormatException("요청하신 정보를 열람 할 수 없습니다.");
        }

        return MarginRecordVo.toVo(marginRecordEntity);
    }

    public Object searchListByWorkspaceId(Map<String, Object> params) {
        if (!userService.isLogin()) {
            throw new InvalidUserAuthException("로그인이 필요한 서비스 입니다.");
        }

        UUID WORKSPACE_ID = null;

        if (params.get("workspaceId") == null) {
            throw new NotMatchedFormatException("정상적인 요청이 아닙니다.");
        }

        UUID USER_ID = userService.getUserId();
        try {
            WORKSPACE_ID = UUID.fromString(params.get("workspaceId").toString());
        } catch (IllegalArgumentException e) {
            throw new NotMatchedFormatException("정상적인 요청이 아닙니다.");
        }

        if (!workspaceMemberService.isAccessedReadPermissionOfWorkspace(WORKSPACE_ID, USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        List<MarginRecordEntity> marginRecordEntities = marginRecordService.searchList(params);
        List<MarginRecordVo> marginRecordVos = marginRecordEntities.stream().map(r->{
            return MarginRecordVo.toVo(r);
        }).collect(Collectors.toList());
        return marginRecordVos;
    }

    public void deleteOne(MarginRecordDto marginRecordDto) {
        if (!userService.isLogin()) {
            throw new InvalidUserAuthException("로그인이 필요한 서비스 입니다.");
        }

        UUID USER_ID = userService.getUserId();

        Map<String, Object> params = new HashMap<>();
        params.put("marginRecordId", marginRecordDto.getId());

        MarginRecordEntity marginRecordEntity = marginRecordService.searchList(params).stream().findFirst().orElseThrow(()-> new NotMatchedFormatException("요청하신 정보를 찾을 수 없습니다."));

        UUID WORKSPACE_ID = marginRecordEntity.getWorkspaceId();

        if (!workspaceMemberService.isAccessedDeletePermissionOfWorkspace(WORKSPACE_ID, USER_ID)) {
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

        marginRecordService.deleteOne(marginRecordEntity.getCid());
    }
}
