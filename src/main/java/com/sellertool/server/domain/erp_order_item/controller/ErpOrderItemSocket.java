package com.sellertool.server.domain.erp_order_item.controller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.sellertool.server.domain.erp_order_item.service.ErpOrderItemBusinessService;
import com.sellertool.server.domain.exception.dto.AccessDeniedPermissionException;
import com.sellertool.server.domain.message.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/ws/v1/erp-order-items")
public class ErpOrderItemSocket {
    //    TODO : 소켓통신 보완해야됨.
    private final ErpOrderItemBusinessService erpOrderItemBusinessService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ErpOrderItemSocket(
            ErpOrderItemBusinessService erpOrderItemBusinessService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.erpOrderItemBusinessService = erpOrderItemBusinessService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * POST URL => /ws/v1/erp-order-items/batch
     * parameters => [*workspaceId]
     *
     * @param itemDtos
     * @param params
     */
    @RequiredLogin
    @PostMapping("/batch")
    public ResponseEntity<?> createBatch(
            @RequestBody @Valid List<ErpOrderItemDto> itemDtos,
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();

        UUID workspaceId = null;

        try{
            workspaceId = UUID.fromString(params.get("workspaceId").toString());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new AccessDeniedPermissionException("워크스페이스 접근 권한이 없습니다.");
        }

        erpOrderItemBusinessService.createBatch(workspaceId, itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setSocketMemo("[주문 수집 관리] 에 추가된 데이터가 있습니다.");

        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("")
    public void updateOne(@RequestBody @Valid ErpOrderItemDto itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.updateOne(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
    }

    @PatchMapping("/batch/option-code/all")
    public void changeBatchForAllOptionCode(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForAllOptionCode(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
    }

    @PatchMapping("/batch/release-option-code")
    public void changeBatchForReleaseOptionCode(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForReleaseOptionCode(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
    }

    @PatchMapping("/batch/sales-yn")
    public void changeBatchForSalesYn(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForSalesYn(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
    }

    @PatchMapping("/batch/release-yn")
    public void changeBatchForReleaseYn(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.changeBatchForReleaseYn(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
    }

//    @PatchMapping("/batch/stock/action-reflect")
//    public ResponseEntity<?> actionReflectStock(@RequestBody List<ErpOrderItemDto> itemDtos){
//        Message message = new Message();
//
//        Integer count = erpOrderItemBusinessService.actionReflectStock(itemDtos);
//        message.setStatus(HttpStatus.OK);
//        message.setMessage("success");
//        message.setMemo(count + " 건의 데이터가 재고 반영 되었습니다.");
//
//        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
//        return new ResponseEntity<>(message, message.getStatus());
//    }
//
//    @PatchMapping("/batch/stock/action-cancel")
//    public ResponseEntity<?> actionCancelStock(@RequestBody List<ErpOrderItemDto> itemDtos){
//        Message message = new Message();
//
//        Integer count = erpOrderItemBusinessService.actionCancelStock(itemDtos);
//        message.setStatus(HttpStatus.OK);
//        message.setMessage("success");
//        message.setMemo(count + " 건의 데이터가 재고 취소 되었습니다.");
//
//        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
//        return new ResponseEntity<>(message, message.getStatus());
//    }

    @PostMapping("/batch-delete")
    public void deleteBatch(@RequestBody List<ErpOrderItemDto> itemDtos) {
        Message message = new Message();

        erpOrderItemBusinessService.deleteBatch(itemDtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
    }

//    @PatchMapping(value = "/batch/waybill")
//    public ResponseEntity<?> changeBatchForWaybill(
//            @RequestPart(value = "file") MultipartFile file, @RequestPart(value = "orderItems") List<ErpOrderItemDto> data
//    ) {
//        Message message = new Message();
//
//        List<WaybillExcelFormDto> waybillExcelFormDtos = erpOrderItemBusinessService.readWaybillExcelFile(file);
//        int updatedCount = erpOrderItemBusinessService.changeBatchForWaybill(data, waybillExcelFormDtos);
//        message.setStatus(HttpStatus.OK);
//        message.setMessage("success");
//        message.setMemo("운송장이 입력된 데이터는 총 : " + updatedCount + " 건 입니다.");
//
//        messagingTemplate.convertAndSend("/topic/erp.erp-order-item", message);
//
//        return new ResponseEntity<>(message, message.getStatus());
//    }
}
