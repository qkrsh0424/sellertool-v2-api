package com.sellertool.server.domain.erp_order_item.service;

import com.sellertool.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.sellertool.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.sellertool.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.sellertool.server.domain.erp_order_item.vo.ErpOrderItemVo;
import com.sellertool.server.domain.exception.dto.CustomExcelFileUploadException;
import com.sellertool.server.domain.option.entity.OptionEntity;
import com.sellertool.server.domain.option.service.OptionService;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.WorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberService;
import com.sellertool.server.utils.CustomDateUtils;
import com.sellertool.server.utils.CustomUniqueKeyUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ErpOrderItemBusinessService {
    private final WorkspaceMemberService workspaceMemberService;
    private final ErpOrderItemService erpOrderItemService;
    private final OptionService optionService;
//    private final ProductReleaseService productReleaseService;
//    private final OptionPackageService optionPackageService;
    private final UserService userService;

    // Excel file extension.
    private final List<String> EXTENSIONS_EXCEL = Arrays.asList("xlsx", "xls");

    /**
     * <b>Extension Check</b>
     * <p>
     *
     * @param file : MultipartFile
     */
    /*
    TODO : 로그인 체크 및 권한 체크 넣어야됨.
     */
    public void isExcelFile(MultipartFile file) {
//        // access check
//        userService.userLoginCheck();

        String extension = FilenameUtils.getExtension(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase());

        if (EXTENSIONS_EXCEL.contains(extension)) {
            return;
        }
        throw new CustomExcelFileUploadException("This is not an excel file.");
    }

    /**
     * <b>Upload Excel File</b>
     * <p>
     * 피아르 엑셀 파일을 업로드한다.
     *
     * @param file : MultipartFile
     * @return List::ErpOrderItemVo::
     */
//    RE-OK
    /*
    TODO : 로그인 체크 및 권한 체크 넣어야됨.
     */
    public List<ErpOrderItemVo> uploadErpOrderExcel(MultipartFile file) {
        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();

        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new CustomExcelFileUploadException("피아르 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요.");
        }

        Sheet sheet = workbook.getSheetAt(0);

        List<ErpOrderItemVo> vos;
        try {
            vos = ErpOrderItemVo.excelSheetToVos(sheet);
        } catch (NullPointerException e) {
            throw new CustomExcelFileUploadException("엑셀 파일 데이터에 올바르지 않은 값이 존재합니다.");
        } catch (IllegalStateException e) {
            throw new CustomExcelFileUploadException("피아르 엑셀 양식과 데이터 타입이 다른 값이 존재합니다.\n올바른 엑셀 파일을 업로드해주세요.");
        } catch (IllegalArgumentException e) {
            throw new CustomExcelFileUploadException("피아르 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요.");
        }

        return vos;
    }

    /*
    workspaceMemberM2OJProj 불러오기
    워크스페이스 존재 여부 확인
    워크스페이스 멤버 여부 확인
    워크스페이스 멤버 쓰기 권한 확인
     */
    @Transactional
    public void createBatch(UUID workspaceId, List<ErpOrderItemDto> orderItemDtos) {
        UUID USER_ID = userService.getUserId();
        /*
        workspaceMemberM2OJProj 불러오기
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
        워크스페이스 멤버 쓰기 권한 확인
         */
        WorkspaceMemberEntity.hasWritePermission(workspaceMemberEntity);

        List<ErpOrderItemDto> newOrderItemDtos = this.itemDuplicationCheck(orderItemDtos);

        List<ErpOrderItemEntity> orderItemEntities = newOrderItemDtos.stream()
                .map(r -> {
                    r.setId(UUID.randomUUID())
                            .setUniqueCode(CustomUniqueKeyUtils.generateKey("p"))
                            .setFreightCode(CustomUniqueKeyUtils.generateFreightCode())
                            .setSalesYn("n")
                            .setReleaseOptionCode(r.getOptionCode())
                            .setReleaseYn("n")
                            .setStockReflectYn("n")
                            .setCreatedAt(CustomDateUtils.getCurrentDateTime())
                            .setCreatedBy(USER_ID)
                            .setWorkspaceCid(workspaceEntity.getCid())
                            .setWorkspaceId(workspaceEntity.getId())
                    ;


                    return ErpOrderItemEntity.toEntity(r);
                }).collect(Collectors.toList());
        erpOrderItemService.bulkInsert(orderItemEntities);
    }

    /*
    로직 분리 부분 한번 더 고려 해보기
     */
    public List<ErpOrderItemDto> itemDuplicationCheck(List<ErpOrderItemDto> dtos) {
        List<ErpOrderItemDto> newItems = dtos.stream().filter(r -> r.getOrderNumber1().isEmpty()).collect(Collectors.toList());
        List<ErpOrderItemDto> duplicationCheckItems = dtos.stream().filter(r -> !r.getOrderNumber1().isEmpty()).collect(Collectors.toList());

        if(duplicationCheckItems.isEmpty()){
            return dtos;
        }

        List<String> orderNumber1 = new ArrayList<>();
        List<String> receiver = new ArrayList<>();
        List<String> prodName = new ArrayList<>();
        List<String> optionName = new ArrayList<>();
        List<Integer> unit = new ArrayList<>();
        duplicationCheckItems.forEach(r -> {
            orderNumber1.add(r.getOrderNumber1());
            receiver.add(r.getReceiver());
            prodName.add(r.getProdName());
            optionName.add(r.getOptionName());
            unit.add(r.getUnit());
        });

        List<ErpOrderItemEntity> duplicationEntities = erpOrderItemService.findDuplicationItems(orderNumber1, receiver, prodName, optionName, unit);

        if (duplicationEntities.size() == 0) {
            return dtos;
        } else {
            for (ErpOrderItemDto duplicationCheckItem : duplicationCheckItems) {
                boolean duplication = false;
                // 주문번호 + 수령인 + 상품명 + 옵션명 + 수량 이 동일하다면 저장 제외
                for (ErpOrderItemEntity duplicationEntity : duplicationEntities) {
                    if (duplicationEntity.getOrderNumber1().equals(duplicationCheckItem.getOrderNumber1())
                            && duplicationEntity.getReceiver().equals(duplicationCheckItem.getReceiver())
                            && duplicationEntity.getProdName().equals(duplicationCheckItem.getProdName())
                            && duplicationEntity.getOptionName().equals(duplicationCheckItem.getOptionName())
                            && duplicationEntity.getUnit().equals(duplicationCheckItem.getUnit())) {
                        duplication = true;
                        break;
                    }
                }
                if (!duplication) {
                    newItems.add(duplicationCheckItem);
                }
            }
        }
        return newItems;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 아이디 리스트 별 ErpOrderItemProjs 데이터를 가져온다.
     * 옵션 재고 수량 추가 및 vos 변환
     * <p>
     *
     * @param params : Map::String, Object::
     * @return List::ErpOrderItemVo::
     */
//    RE-OK
    /*
    TODO : 로그인 체크 및 권한 체크 넣어야됨.
     */
//    @Transactional(readOnly = true)
//    public List<ErpOrderItemVo> searchList(Map<String, Object> params) {
//        // access check
////        userService.userLoginCheck();
//
//        // 등록된 모든 엑셀 데이터를 조회한다
//        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJ(params);       // 페이징 처리 x
//        // 옵션재고수량 추가 및 vos 변환
//
//        return this.setOptionStockUnitAndToVos(itemProjs);
//    }

    /*
    아이디 리스트 별 ErpOrderItemProjs 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    /*
    TODO : 로그인 체크 및 권한 체크 넣어야됨.
     */
    @Transactional(readOnly = true)
    public List<ErpOrderItemVo> searchListByIds(List<UUID> ids, Map<String, Object> params) {
        // access check
//        userService.userLoginCheck();

        // 등록된 모든 엑셀 데이터를 조회한다
        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJ(ids, params);       // 페이징 처리 x
        // 옵션재고수량 추가 및 vos 변환
        return this.setOptionStockUnitAndToVos(itemProjs);
    }

    /*
    조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    /*
    TODO : 로그인 체크 및 권한 체크 넣어야됨.
     */
    @Transactional(readOnly = true)
    public Page<ErpOrderItemVo> searchPage(Map<String, Object> params, Pageable pageable) {
        // access check
//        userService.userLoginCheck();

        /*
        조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
         */
        Page<ErpOrderItemProj> itemPages = erpOrderItemService.findAllM2OJByPage(params, pageable);
        List<ErpOrderItemProj> itemProjs = itemPages.getContent();    // 페이징 처리 o

        // 옵션재고수량 추가 및 vos 변환
        List<ErpOrderItemVo> erpOrderItemVos = this.setOptionStockUnitAndToVos(itemProjs);

        return new PageImpl(erpOrderItemVos, pageable, itemPages.getTotalElements());
    }

    /*
    조건별 페이지별 출고 상태인 ErpOrderItemProj Page 데이터를 가져온다.
    옵션 재고 수량 추가 및 vos 변환
     */
//    RE-OK
    /*
    TODO : 로그인 체크 및 권한 체크 넣어야됨.
     */
//    @Transactional(readOnly = true)
//    public Page<ErpOrderItemVo> searchReleaseItemBatchByPaging(Map<String, Object> params, Pageable pageable) {
//        // access check
////        userService.userLoginCheck();
//
//        Page<ErpOrderItemProj> itemPages = erpOrderItemService.findReleaseItemM2OJByPage(params, pageable);
//        // 등록된 모든 엑셀 데이터를 조회한다
//        List<ErpOrderItemProj> itemProjs = itemPages.getContent();    // 페이징 처리 o
//        // 옵션재고수량 추가 및 vos 변환
//        List<ErpOrderItemVo> erpOrderItemVos = this.setOptionStockUnitAndToVos(itemProjs);
//
//        return new PageImpl(erpOrderItemVos, pageable, itemPages.getTotalElements());
//    }

    /**
     * ErpOrderItemProjs => ErpOrderItemVos
     * proj -> vos 변환 및 재고 수량 셋
     *
     * @param itemProjs
     * @return
     */
    /*
    ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
    옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
    ErpOrderItemProjs -> ErpOrderItemVos 변환
    ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
     */
//    RE-OK
    private List<ErpOrderItemVo> setOptionStockUnitAndToVos(List<ErpOrderItemProj> itemProjs) {
        /*
        ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
         */
        List<OptionEntity> optionEntities = OptionEntity.getExistList(itemProjs);
        /*
        옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
         */
        optionService.setReceivedAndReleasedAndStockSum(optionEntities);
        /*
        ErpOrderItemProjs -> ErpOrderItemVos 변환
         */
        List<ErpOrderItemVo> erpOrderItemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());
        /*
        ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
         */
        ErpOrderItemVo.setOptionStockUnitForList(erpOrderItemVos, optionEntities);

        return erpOrderItemVos;
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 엑셀 데이터의 salesYn(판매 여부)을 업데이트한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     */
//    PASS
    /*
    로그인 체크 및 권한 체크 해야됨.
     */
    @Transactional
    public void changeBatchForSalesYn(List<ErpOrderItemDto> itemDtos) {
        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();

        List<UUID> idList = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        List<ErpOrderItemEntity> entities = erpOrderItemService.findAllByIdList(idList);

        entities.forEach(entity -> itemDtos.forEach(dto -> {
            if (entity.getId().equals(dto.getId())) {
                entity.setSalesYn(dto.getSalesYn()).setSalesAt(dto.getSalesAt());
            }
        }));

        erpOrderItemService.saveListAndModify(entities);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 엑셀 데이터의 releaseYn(출고 여부)을 업데이트한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @see ErpOrderItemService#findAllByIdList
     * @see CustomDateUtils#getCurrentDateTime
     * @see ErpOrderItemService#saveListAndModify
     */
//    PASS
    /*
    로그인 체크 및 권한 체크 해야됨.
     */
    @Transactional
    public void changeBatchForReleaseYn(List<ErpOrderItemDto> itemDtos) {
        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();

        List<UUID> idList = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        List<ErpOrderItemEntity> entities = erpOrderItemService.findAllByIdList(idList);

        entities.forEach(entity -> itemDtos.forEach(dto -> {
            if (entity.getId().equals(dto.getId())) {
                entity.setReleaseYn(dto.getReleaseYn()).setReleaseAt(dto.getReleaseAt());
            }
        }));

        erpOrderItemService.saveListAndModify(entities);
    }

    /**
     * <b>Data Delete Related Method</b>
     * <p>
     * 피아르 엑셀 데이터를 삭제한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @see ErpOrderItemEntity#toEntity
     * @see ErpOrderItemService#delete
     */
//    PASS
    /*
    로그인 체크 및 권한 체크 해야됨.
     */
    @Transactional
    public void deleteBatch(List<ErpOrderItemDto> itemDtos) {
        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();

        List<UUID> itemIds = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        erpOrderItemService.deleteBatch(itemIds);
    }

    /**
     * <b>Data Update Related Method</b>
     * <p>
     * 피아르 엑셀 데이터를 수정한다.
     *
     * @param dto : ErpOrderItemDto
     * @see ErpOrderItemService#searchOne
     * @see ErpOrderItemService#saveAndModify
     */
//    PASS
    /*
    로그인 체크 및 권한 체크 해야됨.
     */
    @Transactional
    public void updateOne(ErpOrderItemDto dto) {
        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();

        ErpOrderItemEntity entity = erpOrderItemService.searchOne(dto.getId());

        /*
        Dirty Checking 업데이트
         */
        entity.setProdName(dto.getProdName()).setOptionName(dto.getOptionName())
                .setUnit(dto.getUnit()).setReceiver(dto.getReceiver()).setReceiverContact1(dto.getReceiverContact1())
                .setReceiverContact2(dto.getReceiverContact2())
                .setDestination(dto.getDestination())
                .setSalesChannel(dto.getSalesChannel())
                .setOrderNumber1(dto.getOrderNumber1())
                .setOrderNumber2(dto.getOrderNumber2())
                .setChannelProdCode(dto.getChannelProdCode())
                .setChannelOptionCode(dto.getChannelOptionCode())
                .setZipCode(dto.getZipCode())
                .setCourier(dto.getCourier())
                .setTransportType(dto.getTransportType())
                .setDeliveryMessage(dto.getDeliveryMessage())
                .setWaybillNumber(dto.getWaybillNumber())
                .setPrice(dto.getPrice())
                .setDeliveryCharge(dto.getDeliveryCharge())
                .setBarcode(dto.getBarcode())
                .setProdCode(dto.getProdCode())
                .setOptionCode(dto.getOptionCode())
                .setReleaseOptionCode(dto.getReleaseOptionCode())
                .setManagementMemo1(dto.getManagementMemo1())
                .setManagementMemo2(dto.getManagementMemo2())
                .setManagementMemo3(dto.getManagementMemo3())
                .setManagementMemo4(dto.getManagementMemo4())
                .setManagementMemo5(dto.getManagementMemo5())
                .setManagementMemo6(dto.getManagementMemo6())
                .setManagementMemo7(dto.getManagementMemo7())
                .setManagementMemo8(dto.getManagementMemo8())
                .setManagementMemo9(dto.getManagementMemo9())
                .setManagementMemo10(dto.getManagementMemo10());
    }


    /**
     * <b>Data Update Related Method</b>
     * <p>
     * 변경 주문 옵션코드를 참고해 주문 옵션코드와 출고 옵션코드를 변경한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @see ErpOrderItemService#findAllByIdList
     * @see ErpOrderItemService#saveListAndModify
     */
    /*
    로그인 체크 및 권한 체크 해야됨.
     */
    @Transactional
    public void changeBatchForAllOptionCode(List<ErpOrderItemDto> itemDtos) {
        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();

        List<ErpOrderItemEntity> entities = erpOrderItemService.getEntities(itemDtos);

//        Dirty Checking update
        entities.forEach(entity -> itemDtos.forEach(dto -> {
            if (entity.getId().equals(dto.getId())) {
                entity.setOptionCode(dto.getOptionCode())
                        .setReleaseOptionCode(dto.getOptionCode());
            }
        }));
    }

    /**
     * <b>Data Update Related Method</b>
     * <p>
     * 출고 옵션코드를 변경한다.
     *
     * @param itemDtos : List::ErpOrderItemDto::
     * @see ErpOrderItemService#findAllByIdList
     * @see ErpOrderItemService#saveListAndModify
     */
//    PASS
    /*
    로그인 체크 및 권한 체크 해야됨.
     */
    @Transactional
    public void changeBatchForReleaseOptionCode(List<ErpOrderItemDto> itemDtos) {
        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();

        List<UUID> idList = itemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
        List<ErpOrderItemEntity> entities = erpOrderItemService.findAllByIdList(idList);

        /*
        Dirty Checking update
         */
        entities.forEach(entity -> itemDtos.forEach(dto -> {
            if (entity.getId().equals(dto.getId())) {
                entity.setReleaseOptionCode(dto.getReleaseOptionCode());
            }
        }));
    }

    /**
     * 운송장 업로드 엑셀 파일을 읽어들이고 해당 엑셀 파일을 DTO로 변환해서 리턴해준다.
     * <p>Last Modifier : Austin Park 22.4.22</p>
     *
     * @since 1.1
     * @see ErpOrderItemBusinessService#changeBatchForWaybill
     */
    /*
    로그인 체크 및 권한 체크 해야됨.
     */
//    @Transactional(readOnly = true)
//    public List<WaybillExcelFormDto> readWaybillExcelFile(MultipartFile file) {
//        // access check
////        userService.userLoginCheck();
////        userService.userManagerRoleCheck();
//
//        if (!CustomExcelUtils.isExcelFile(file)) {
//            throw new CustomExcelFileUploadException("올바른 파일은 업로드 해주세요.\n[.xls, .xlsx] 확장자 파일만 허용됩니다.");
//        }
//
//        List<String> HEADER_NAMES = WaybillExcelFormManager.HEADER_NAMES;
//        List<String> FIELD_NAMES = WaybillExcelFormManager.getAllFieldNames();
//        List<Integer> REQUIRED_CELL_NUMBERS = WaybillExcelFormManager.REQUIRED_CELL_NUMBERS;
//
//        int SHEET_INDEX = 0;
//        Integer HEADER_ROW_INDEX = WaybillExcelFormManager.HEADER_ROW_INDEX;
//        Integer DATA_START_ROW_INDEX = WaybillExcelFormManager.DATA_START_ROW_INDEX;
//        Integer ALLOWED_CELL_SIZE = WaybillExcelFormManager.ALLOWED_CELL_SIZE;
//
//        Workbook workbook = CustomExcelUtils.getWorkbook(file);
//        Sheet worksheet = workbook.getSheetAt(SHEET_INDEX);
//        Row headerRow = worksheet.getRow(HEADER_ROW_INDEX);
//
//        /*
//        최종 데이터 담는 리스트 생성
//         */
//        List<WaybillExcelFormDto> waybillExcelFormDtos = new ArrayList<>();
//
//        /*
//        엑셀 형식 검사 => cell size, header cell name check
//         */
//        if (
//                !CustomExcelUtils.getCellCount(worksheet, HEADER_ROW_INDEX).equals(ALLOWED_CELL_SIZE) ||
//                        !CustomExcelUtils.isCheckedHeaderCell(headerRow, HEADER_NAMES)
//        ) {
//            throw new CustomExcelFileUploadException("올바른 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드 해주세요.");
//        }
//
//        /*
//        엑셀 데이터 부분 컨트롤 Row Loop
//         */
//        for (int i = DATA_START_ROW_INDEX; i < worksheet.getPhysicalNumberOfRows(); i++) {
//            Row row = worksheet.getRow(i);
//            WaybillExcelFormDto waybillExcelFormDto = new WaybillExcelFormDto();
//
//            /*
//            Cell Loop
//             */
//            for (int j = 0; j < ALLOWED_CELL_SIZE; j++) {
//                Cell cell = row.getCell(j);
//                CellType cellType = cell.getCellType();
//                Object cellValue;
//                /*
//                필수 데이터 셀이 하나라도 비어있게 되면 dto를 Null로 처리하고 break
//                 */
//                if (REQUIRED_CELL_NUMBERS.contains(j) && cellType.equals(CellType.BLANK)) {
//                    waybillExcelFormDto = null;
//                    break;
//                }
//                /*
//                cellValue 가져오기
//                 */
//                cellValue = CustomExcelUtils.getCellValueObject(cell, CustomExcelUtils.NUMERIC_TO_INT);
//                /*
//                cellValue dto에 매핑시키기
//                 */
//                CustomFieldUtils.setFieldValueWithSuper(waybillExcelFormDto, FIELD_NAMES.get(j), cellValue.toString());
//            }
//            /*
//            dto가 널이 아니라면 리스트에 담는다.
//             */
//            if (waybillExcelFormDto != null) {
//                waybillExcelFormDtos.add(waybillExcelFormDto);
//            }
//        }
//
//        return waybillExcelFormDtos;
//    }

    /**
     * <p>선택된 데이터와 운송장 일괄 등록을 위해 엑셀에서 dto 로 변환된 데이터를 비교 대조해서 선택된 데이터의 운송장 관련 데이터들을 업데이트 한다.</p>
     * <p>일반적으로 ErpOrderItemBusinessService#readWaybillExcelFile의 작업이 선행 되어야한다. </p>
     * <p>Last Modifier : Austin Park 22.4.22</p>
     *
     * @return 운송장 관련 데이터가 정상적으로 적용된 데이터의 수
     * @since 1.1
     * @see ErpOrderItemBusinessService#readWaybillExcelFile
     */
//    @Transactional
//    public int changeBatchForWaybill(List<ErpOrderItemDto> erpOrderItemDtos, List<WaybillExcelFormDto> waybillExcelFormDtos) {
//        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();
//
//        /*
//        수취인명 별 운임번호 분해작업
//         */
//        List<WaybillExcelFormDto> dismantledWaybillExcelFormDtos = new ArrayList<>();
//        waybillExcelFormDtos.forEach(r -> {
//            List<String> freightCodes = List.of(r.getFreightCode().split(","));
//
//            freightCodes.forEach(freightCode -> {
//                WaybillExcelFormDto dto = new WaybillExcelFormDto();
//                dto.setReceiver(r.getReceiver());
//                dto.setFreightCode(freightCode);
//                dto.setWaybillNumber(r.getWaybillNumber());
//                dto.setTransportType(r.getTransportType());
//                dto.setCourier(r.getCourier());
//
//                dismantledWaybillExcelFormDtos.add(dto);
//            });
//        });
//
//        /*
//        선택된 주문건의 엔터티들을 불러온다.
//         */
//        List<UUID> ids = erpOrderItemDtos.stream().map(ErpOrderItemDto::getId).collect(Collectors.toList());
//        List<ErpOrderItemEntity> erpOrderItemEntities = erpOrderItemService.findAllByIdList(ids);
//        AtomicInteger updatedCount = new AtomicInteger();
//
//        /*
//        분해작업이 완료된 운임코드+수취인명과 선택된 주문건들의 운임코드+수취인명을 비교해서 일치하는 데이터에 운송장 관련 데이터들을 업데이트한다.
//        업데이트 완료된건 카운팅
//         */
//        erpOrderItemEntities.forEach(erpOrderItemEntity -> {
//            String matchingData = erpOrderItemEntity.getReceiver() + erpOrderItemEntity.getFreightCode();
//            dismantledWaybillExcelFormDtos.forEach(waybillExcelFormDto -> {
//                String matchedData = waybillExcelFormDto.getReceiver() + waybillExcelFormDto.getFreightCode();
//
//                if (matchingData.equals(matchedData)) {
//                    erpOrderItemEntity.setWaybillNumber(waybillExcelFormDto.getWaybillNumber());
//                    erpOrderItemEntity.setTransportType(waybillExcelFormDto.getTransportType());
//                    erpOrderItemEntity.setCourier(waybillExcelFormDto.getCourier());
//                    updatedCount.getAndIncrement();
//                }
//            });
//        });
//
//        return updatedCount.get();
//    }

    /**
     * 선택된 데이터들의 재고를 반영한다.
     *
     * TODO : 현재 세트상품과 세트상품이 아닌 데이터들을 구분해서 insert 쿼리를 날리는데 이렇게되면 insert쿼리가 두번이 나누어져서 던져지게됨. 세트상품을 먼저 분해해서 일반 옵션 엔터티로 만들어서 한번에 던지는게 낫지않을까?
     */
//    @Transactional
//    public Integer actionReflectStock(List<ErpOrderItemDto> itemDtos) {
//        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();
//
//        Set<String> optionCodeSet = new HashSet<>();
//        List<ErpOrderItemEntity> erpOrderItemEntities = erpOrderItemService.getEntities(itemDtos);
//        List<ProductOptionEntity> productOptionEntities;
//        AtomicInteger count = new AtomicInteger();
//
//        for (ErpOrderItemEntity r : erpOrderItemEntities) {
//            if (!r.getReleaseOptionCode().isEmpty()) {
//                optionCodeSet.add(r.getReleaseOptionCode());
//            }
//        }
//
//        List<ProductOptionGetDto> productOptionGetDtos = optionService.searchListByOptionCodes(new ArrayList<>(optionCodeSet));
//        productOptionEntities = productOptionGetDtos.stream().map(ProductOptionEntity::toEntity).collect(Collectors.toList());
//
//        // 1. 세트상품 X
//        List<ProductOptionEntity> originOptionEntities = productOptionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
//        // 2. 세트상품 O
//        List<ProductOptionEntity> parentOptionEntities = productOptionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());
//
//        count.addAndGet(this.reflectStockUnit(erpOrderItemEntities, originOptionEntities));
//        count.addAndGet(this.reflectStockUnitOfPackageOption(erpOrderItemEntities, parentOptionEntities));
//        return count.get();
//    }

//    public int reflectStockUnit(List<ErpOrderItemEntity> erpOrderItemEntities, List<ProductOptionEntity> originOptionEntities) {
//        List<ProductReleaseEntity> releaseEntities = new ArrayList<>();
//        AtomicInteger count = new AtomicInteger();
//
//        for (ErpOrderItemEntity orderItemEntity : erpOrderItemEntities) {
//            originOptionEntities.forEach(optionEntity -> {
//                if (optionEntity.getCode().equals(orderItemEntity.getReleaseOptionCode()) && orderItemEntity.getStockReflectYn().equals("n")) {
//                    count.getAndIncrement();
//                    ProductReleaseEntity releaseEntity = new ProductReleaseEntity();
//
//                    releaseEntity.setId(UUID.randomUUID());
//                    releaseEntity.setErpOrderItemId(orderItemEntity.getId());
//                    releaseEntity.setReleaseUnit(orderItemEntity.getUnit());
//                    releaseEntity.setMemo("");
//                    releaseEntity.setCreatedAt(CustomDateUtils.getCurrentDateTime());
//                    releaseEntity.setCreatedBy(orderItemEntity.getCreatedBy());
//                    releaseEntity.setProductOptionCid(optionEntity.getCid());
//                    releaseEntity.setProductOptionId(optionEntity.getId());
//
//                    orderItemEntity.setStockReflectYn("y");
//                    releaseEntities.add(releaseEntity);
//                }
//            });
//        }
//
//        productReleaseService.bulkInsert(releaseEntities);
//        return count.get();
//    }

//    public int reflectStockUnitOfPackageOption(List<ErpOrderItemEntity> erpOrderItemEntities, List<ProductOptionEntity> parentOptionEntities) {
//        List<ProductReleaseEntity> releaseEntities = new ArrayList<>();
//        AtomicInteger count = new AtomicInteger();
//
//        // 1. 해당 옵션에 포함되는 하위 패키지 옵션들 추출
//        List<UUID> parentOptionIdList = parentOptionEntities.stream().map(ProductOptionEntity::getId).collect(Collectors.toList());
//        // 2. 구성된 옵션패키지 추출 - 여러 패키지들이 다 섞여있는 상태
//        List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);
//
//        for (ErpOrderItemEntity orderItemEntity : erpOrderItemEntities) {
//            parentOptionEntities.forEach(parentOption -> {
//                if (parentOption.getCode().equals(orderItemEntity.getReleaseOptionCode()) && orderItemEntity.getStockReflectYn().equals("n")) {
//                    optionPackageEntities.forEach(option -> {
//                        if (option.getParentOptionId().equals(parentOption.getId())) {
//                            count.getAndIncrement();
//
//                            ProductReleaseEntity releaseEntity = new ProductReleaseEntity();
//                            releaseEntity.setId(UUID.randomUUID());
//                            releaseEntity.setErpOrderItemId(orderItemEntity.getId());
//                            releaseEntity.setReleaseUnit(orderItemEntity.getUnit() * option.getPackageUnit());
//                            releaseEntity.setMemo("");
//                            releaseEntity.setCreatedAt(CustomDateUtils.getCurrentDateTime());
//                            releaseEntity.setCreatedBy(orderItemEntity.getCreatedBy());
//                            releaseEntity.setProductOptionCid(option.getOriginOptionCid());
//                            releaseEntity.setProductOptionId(option.getOriginOptionId());
//
//                            orderItemEntity.setStockReflectYn("y");
//                            releaseEntities.add(releaseEntity);
//                        }
//                    });
//                }
//            });
//        }
//
//        productReleaseService.bulkInsert(releaseEntities);
//        return count.get();
//    }

    /**
     * 선택된 데이터들중 이미 재고가 반영된 데이터들을 대상으로 재고 반영을 취소시킨다.
     * @param itemDtos
     * @return
     */
//    @Transactional
//    public Integer actionCancelStock(List<ErpOrderItemDto> itemDtos) {
//        // access check
//        userService.userLoginCheck();
//        userService.userManagerRoleCheck();
//
//        /*
//        재고 반영이 완료된 데이터들을 가져온다.
//         */
//        itemDtos = itemDtos.stream().filter(r -> r.getStockReflectYn().equals("y")).collect(Collectors.toList());
//        List<ErpOrderItemEntity> erpOrderItemEntities = erpOrderItemService.getEntities(itemDtos);
//        List<UUID> erpOrderItemIds = new ArrayList<>();
//        AtomicInteger count = new AtomicInteger();
//
//        /*
//        재고에 이미 반영된 데이터들의 재고반영 여부를 업데이트하고, 재고에 이미 반영된 데이터의 아이디 값들을 가져온다.
//         */
//        for (ErpOrderItemEntity orderItemEntity : erpOrderItemEntities) {
//            count.getAndIncrement();
//            erpOrderItemIds.add(orderItemEntity.getId());
//            orderItemEntity.setStockReflectYn("n");
//        }
//
//        /*
//        이미 재고가 반영되었던 데이터들의 아이디값을 이용해서 재고 반영 여부에서 해당 데이터들을 삭제한다.
//         */
//        productReleaseService.deleteByErpOrderItemIds(erpOrderItemIds);
//        return count.get();
//    }
}
