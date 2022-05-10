package com.sellertool.server.domain.erp_order_item.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sellertool.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.sellertool.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.sellertool.server.domain.exception.dto.CustomExcelFileUploadException;
import com.sellertool.server.domain.option.entity.OptionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class ErpOrderItemVo {
    private UUID id;
    private String uniqueCode; // 피아르 고유코드
    private String prodName; // 상품명 / 필수값
    private String optionName; // 옵션정보 / 필수값
    private String unit; // 수량 / 필수값
    private String receiver; // 수취인명 / 필수값
    private String receiverContact1; // 전화번호1 / 필수값
    private String receiverContact2; // 전화번호2
    private String destination; // 주소 / 필수값
    private String salesChannel; // 판매채널
    private String orderNumber1; // 판매채널 주문번호1
    private String orderNumber2; // 판매채널 주문번호2
    private String channelProdCode; // 판매채널 상품코드
    private String channelOptionCode; // 판매채널 옵션코드
    private String zipCode; // 우편번호
    private String courier; // 택배사
    private String transportType; // 배송방식
    private String deliveryMessage; // 배송메세지
    private String waybillNumber;   // 운송장번호
    private String price;  // 판매금액
    private String deliveryCharge;  // 배송비
    private String barcode; // 바코드
    private String prodCode; // 피아르 상품코드
    private String optionCode; // 피아르 옵션코드
    private String releaseOptionCode;   // 출고 옵션코드
    private String managementMemo1; // 관리메모1
    private String managementMemo2; // 관리메모2
    private String managementMemo3; // 관리메모3
    private String managementMemo4; // 관리메모4
    private String managementMemo5; // 관리메모5
    private String managementMemo6; // 관리메모6
    private String managementMemo7; // 관리메모7
    private String managementMemo8; // 관리메모8
    private String managementMemo9; // 관리메모9
    private String managementMemo10; // 관리메모10
    private String freightCode; // 운송코드

    private String salesYn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime salesAt;

    private String releaseYn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime releaseAt;

    private String stockReflectYn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    private UUID createdBy;

    private String categoryName;
    private String prodDefaultName;
    private String prodManagementName;
    private String optionDefaultName;
    private String optionManagementName;
    private String optionStockUnit;

    public static ErpOrderItemVo toVo(ErpOrderItemProj proj) {
        if (proj == null)
            return null;

        ErpOrderItemVo itemVo = ErpOrderItemVo.builder()
                .id(proj.getErpOrderItemEntity().getId())
                .uniqueCode(proj.getErpOrderItemEntity().getUniqueCode())
                .prodName(proj.getErpOrderItemEntity().getProdName())
                .optionName(proj.getErpOrderItemEntity().getOptionName())
                .unit(proj.getErpOrderItemEntity().getUnit().toString())
                .receiver(proj.getErpOrderItemEntity().getReceiver())
                .receiverContact1(proj.getErpOrderItemEntity().getReceiverContact1())
                .receiverContact2(proj.getErpOrderItemEntity().getReceiverContact2())
                .destination(proj.getErpOrderItemEntity().getDestination())
                .salesChannel(proj.getErpOrderItemEntity().getSalesChannel())
                .orderNumber1(proj.getErpOrderItemEntity().getOrderNumber1())
                .orderNumber2(proj.getErpOrderItemEntity().getOrderNumber2())
                .channelProdCode(proj.getErpOrderItemEntity().getChannelProdCode())
                .channelOptionCode(proj.getErpOrderItemEntity().getChannelOptionCode())
                .zipCode(proj.getErpOrderItemEntity().getZipCode())
                .courier(proj.getErpOrderItemEntity().getCourier())
                .transportType(proj.getErpOrderItemEntity().getTransportType())
                .deliveryMessage(proj.getErpOrderItemEntity().getDeliveryMessage())
                .waybillNumber(proj.getErpOrderItemEntity().getWaybillNumber())
                .price(proj.getErpOrderItemEntity().getPrice() != null ? proj.getErpOrderItemEntity().getPrice().toString() : null)
                .deliveryCharge(proj.getErpOrderItemEntity().getDeliveryCharge() != null ? proj.getErpOrderItemEntity().getDeliveryCharge().toString() : null)
                .barcode(proj.getErpOrderItemEntity().getBarcode())
                .prodCode(proj.getErpOrderItemEntity().getProdCode())
                .optionCode(proj.getErpOrderItemEntity().getOptionCode())
                .releaseOptionCode(proj.getErpOrderItemEntity().getReleaseOptionCode())
                .managementMemo1(proj.getErpOrderItemEntity().getManagementMemo1())
                .managementMemo2(proj.getErpOrderItemEntity().getManagementMemo2())
                .managementMemo3(proj.getErpOrderItemEntity().getManagementMemo3())
                .managementMemo4(proj.getErpOrderItemEntity().getManagementMemo4())
                .managementMemo5(proj.getErpOrderItemEntity().getManagementMemo5())
                .managementMemo6(proj.getErpOrderItemEntity().getManagementMemo6())
                .managementMemo7(proj.getErpOrderItemEntity().getManagementMemo7())
                .managementMemo8(proj.getErpOrderItemEntity().getManagementMemo8())
                .managementMemo9(proj.getErpOrderItemEntity().getManagementMemo9())
                .managementMemo10(proj.getErpOrderItemEntity().getManagementMemo10())
                .freightCode(proj.getErpOrderItemEntity().getFreightCode())
                .salesYn(proj.getErpOrderItemEntity().getSalesYn())
                .salesAt(proj.getErpOrderItemEntity().getSalesAt())
                .releaseYn(proj.getErpOrderItemEntity().getReleaseYn())
                .releaseAt(proj.getErpOrderItemEntity().getReleaseAt())
                .stockReflectYn(proj.getErpOrderItemEntity().getStockReflectYn())
                .createdAt(proj.getErpOrderItemEntity().getCreatedAt())
                .createdBy(proj.getErpOrderItemEntity().getCreatedBy())
                .categoryName(proj.getCategoryEntity() != null ? proj.getCategoryEntity().getName() : "")
                .prodDefaultName(proj.getProductEntity() != null ? proj.getProductEntity().getDefaultName() : "")
                .prodManagementName(proj.getProductEntity() != null ? proj.getProductEntity().getManagementName() : "")
                .optionDefaultName(proj.getOptionEntity() != null ? proj.getOptionEntity().getDefaultName() : "")
                .optionManagementName(proj.getOptionEntity() != null ? proj.getOptionEntity().getManagementName() : "")
                .build();

        return itemVo;
    }

    public static ErpOrderItemVo toVo(ErpOrderItemDto dto) {
        if(dto == null) return null;

        ErpOrderItemVo itemVo = ErpOrderItemVo.builder()
                .id(dto.getId())
                .uniqueCode(dto.getUniqueCode() != null ? dto.getUniqueCode().toString() : null)
                .prodName(dto.getProdName())
                .optionName(dto.getOptionName())
                .unit(dto.getUnit() != null ? dto.getUnit().toString() : null)
                .receiver(dto.getReceiver())
                .receiverContact1(dto.getReceiverContact1())
                .receiverContact2(dto.getReceiverContact2())
                .destination(dto.getDestination())
                .salesChannel(dto.getSalesChannel())
                .orderNumber1(dto.getOrderNumber1())
                .orderNumber2(dto.getOrderNumber2())
                .channelProdCode(dto.getChannelProdCode())
                .channelOptionCode(dto.getChannelOptionCode())
                .zipCode(dto.getZipCode())
                .courier(dto.getCourier())
                .transportType(dto.getTransportType())
                .deliveryMessage(dto.getDeliveryMessage())
                .waybillNumber(dto.getWaybillNumber())
                .price(dto.getPrice() != null ? dto.getPrice().toString() : null)
                .deliveryCharge(dto.getDeliveryCharge() != null ? dto.getDeliveryCharge().toString() : null)
                .barcode(dto.getBarcode())
                .prodCode(dto.getProdCode())
                .optionCode(dto.getOptionCode())
                .releaseOptionCode(dto.getReleaseOptionCode())
                .managementMemo1(dto.getManagementMemo1())
                .managementMemo2(dto.getManagementMemo2())
                .managementMemo3(dto.getManagementMemo3())
                .managementMemo4(dto.getManagementMemo4())
                .managementMemo5(dto.getManagementMemo5())
                .managementMemo6(dto.getManagementMemo6())
                .managementMemo7(dto.getManagementMemo7())
                .managementMemo8(dto.getManagementMemo8())
                .managementMemo9(dto.getManagementMemo9())
                .managementMemo10(dto.getManagementMemo10())
                .freightCode(dto.getFreightCode())
                .salesYn(dto.getSalesYn())
                .salesAt(dto.getSalesAt())
                .releaseYn(dto.getReleaseYn())
                .releaseAt(dto.getReleaseAt())
                .stockReflectYn(dto.getStockReflectYn())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .build();

        return itemVo;
    }

    public static void setOptionStockUnitForList(List<ErpOrderItemVo> erpOrderItemVos, List<OptionEntity> optionEntities) {
        erpOrderItemVos.forEach(erpOrderItemVo -> {
            optionEntities.forEach(optionEntity ->{
                if(!erpOrderItemVo.getOptionCode().isEmpty() && erpOrderItemVo.getOptionCode().equals(optionEntity.getCode())){
                    erpOrderItemVo.setOptionStockUnit(optionEntity.getStockSumUnit().toString());
                    return;
                }
            });
        });
    }

    public static List<ErpOrderItemVo> excelSheetToVos(Sheet worksheet) {
        Integer PIAAR_ERP_ORDER_ITEM_SIZE = 34;
        Integer PIAAR_ERP_ORDER_MEMO_START_INDEX = 24;

        List<String> PIAAR_ERP_ORDER_HEADER_NAME_LIST = Arrays.asList(
                "피아르 고유번호",
                "상품명",
                "옵션정보",
                "수량",
                "수취인명",
                "전화번호1",
                "전화번호2",
                "주소",
                "판매채널",
                "판매채널 주문번호1",
                "판매채널 주문번호2",
                "판매채널 상품코드",
                "판매채널 옵션코드",
                "우편번호",
                "택배사",
                "배송방식",
                "배송메세지",
                "운송장번호",
                "판매금액",
                "배송비",
                "바코드",
                "피아르 상품코드",
                "피아르 옵션코드",
                "출고 옵션코드",
                "관리메모1",
                "관리메모2",
                "관리메모3",
                "관리메모4",
                "관리메모5",
                "관리메모6",
                "관리메모7",
                "관리메모8",
                "관리메모9",
                "관리메모10"
        );

        List<ErpOrderItemVo> itemVos = new ArrayList<>();

        Row firstRow = worksheet.getRow(0);
        // 피아르 엑셀 양식 검사
        for (int i = 0; i < PIAAR_ERP_ORDER_ITEM_SIZE; i++) {
            Cell cell = firstRow.getCell(i);
            String headerName = cell != null ? cell.getStringCellValue() : null;
            // 지정된 양식이 아니라면
            if (!PIAAR_ERP_ORDER_HEADER_NAME_LIST.get(i).equals(headerName)) {
                throw new CustomExcelFileUploadException("피아르 양식의 엑셀 파일이 아닙니다.\n올바른 엑셀 파일을 업로드해주세요.");
            }
        }

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if (row == null)
                break;

            Object cellValue = new Object();
            List<String> customManagementMemo = new ArrayList<>();

            // type check and data setting of managementMemo1~10.
            for (int j = PIAAR_ERP_ORDER_MEMO_START_INDEX; j < PIAAR_ERP_ORDER_ITEM_SIZE; j++) {
                Cell cell = row.getCell(j);

                if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
                    cellValue = "";
                } else if (cell.getCellType().equals(CellType.NUMERIC)) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Instant instant = Instant.ofEpochMilli(cell.getDateCellValue().getTime());
                        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                        // yyyy-MM-dd'T'HH:mm:ss -> yyyy-MM-dd HH:mm:ss로 변경
                        String newDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        cellValue = newDate;
                    } else {
                        cellValue = cell.getNumericCellValue();
                    }
                } else {
                    cellValue = cell.getStringCellValue();
                }
                customManagementMemo.add(cellValue.toString());
            }

            // price, deliveryCharge - 엑셀 타입 string, number 허용
            String priceStr = (row.getCell(18) == null) ? "0" : (row.getCell(18).getCellType().equals(CellType.NUMERIC) ?
                    Integer.toString((int) row.getCell(18).getNumericCellValue()) : row.getCell(18).getStringCellValue());

            String deliveryChargeStr = (row.getCell(19) == null) ? "0" : (row.getCell(19).getCellType().equals(CellType.NUMERIC) ?
                    Integer.toString((int) row.getCell(19).getNumericCellValue()) : row.getCell(19).getStringCellValue());

            // '출고 옵션코드' 값이 입력되지 않았다면 '피아르 옵션코드'로 대체한다
            String releaseOptionCode = (row.getCell(23) != null) ? row.getCell(23).getStringCellValue() : (row.getCell(22) == null ? "" : row.getCell(22).getStringCellValue());

            ErpOrderItemVo excelVo = ErpOrderItemVo.builder()
                    .uniqueCode(null)
                    .prodName(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                    .optionName(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "")
                    .unit(row.getCell(3) != null ? Integer.toString((int) row.getCell(3).getNumericCellValue()) : "")
                    .receiver(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "")
                    .receiverContact1(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "")
                    .receiverContact2(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "")
                    .destination(row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "")
                    .salesChannel(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "")
                    .orderNumber1(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                    .orderNumber2(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                    .channelProdCode(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "")
                    .channelOptionCode(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "")
                    .zipCode(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : "")
                    .courier(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : "")
                    .transportType(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                    .deliveryMessage(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                    .waybillNumber(row.getCell(17) != null ? row.getCell(17).getStringCellValue() : "")
                    .price(priceStr)
                    .deliveryCharge(deliveryChargeStr)
                    .barcode(row.getCell(20) != null ? row.getCell(20).getStringCellValue() : "")
                    .prodCode(row.getCell(21) != null ? row.getCell(21).getStringCellValue() : "")
                    .optionCode(row.getCell(22) != null ? row.getCell(22).getStringCellValue() : "")
                    .releaseOptionCode(releaseOptionCode)
                    .managementMemo1(customManagementMemo.get(0))
                    .managementMemo2(customManagementMemo.get(1))
                    .managementMemo3(customManagementMemo.get(2))
                    .managementMemo4(customManagementMemo.get(3))
                    .managementMemo5(customManagementMemo.get(4))
                    .managementMemo6(customManagementMemo.get(5))
                    .managementMemo7(customManagementMemo.get(6))
                    .managementMemo8(customManagementMemo.get(7))
                    .managementMemo9(customManagementMemo.get(8))
                    .managementMemo10(customManagementMemo.get(9))
                    .freightCode(null)
                    .build();

            itemVos.add(excelVo);
        }
        return itemVos;
    }
}

