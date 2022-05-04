package com.sellertool.server.domain.option.controller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.message.dto.Message;
import com.sellertool.server.domain.option.dto.OptionDto;
import com.sellertool.server.domain.option.service.OptionBusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/options")
@RequiredArgsConstructor
@Slf4j
public class OptionApiV1 {
    private final OptionBusinessService optionBusinessService;

    /**
     * <p>GET URL => /api/v1/options/products/{productId}</p>
     * <p>parameters => [*workspaceId]</p>
     */
    @RequiredLogin
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> searchList(
            @PathVariable(value = "productId") Object productIdObj,
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();

        UUID workspaceId = null;
        UUID productId = null;
        try {
            workspaceId = UUID.fromString(params.get("workspaceId").toString());
            productId = UUID.fromString(productIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        message.setData(optionBusinessService.searchList(workspaceId, productId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * <p>POST URL => /api/v1/options/products/{productId}</p>
     * <p>parameters => [*workspaceId]</p>
     */
    @RequiredLogin
    @PostMapping("/products/{productId}")
    public ResponseEntity<?> createOne(
            @PathVariable(value = "productId") Object productIdObj,
            @RequestParam Map<String, Object> params,
            @RequestBody OptionDto.CreateRequest optionDto
    ) {
        Message message = new Message();

        UUID workspaceId = null;
        UUID productId = null;
        try {
            workspaceId = UUID.fromString(params.get("workspaceId").toString());
            productId = UUID.fromString(productIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        optionBusinessService.createOne(workspaceId, productId, optionDto);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * <p>PUT URL => /api/v1/options/{optionId}</p>
     * <p>parameters => [*workspaceId]</p>
     */
    @RequiredLogin
    @PutMapping("/{optionId}")
    public ResponseEntity<?> updateOne(
            @PathVariable(value = "optionId") Object optionIdObj,
            @RequestParam Map<String, Object> params,
            @RequestBody OptionDto.UpdateRequest optionDto
    ) {
        Message message = new Message();

        UUID workspaceId = null;
        UUID optionId = null;
        try {
            workspaceId = UUID.fromString(params.get("workspaceId").toString());
            optionId = UUID.fromString(optionIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        optionBusinessService.updateOne(workspaceId, optionId, optionDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * <p>DELETE URL => /api/v1/options/{optionId}</p>
     * <p>parameters => [*workspaceId]</p>
     */
    @RequiredLogin
    @DeleteMapping("/{optionId}")
    public ResponseEntity<?> deleteOne(
            @PathVariable(value = "optionId") Object optionIdObj,
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();

        UUID workspaceId = null;
        UUID optionId = null;
        try {
            workspaceId = UUID.fromString(params.get("workspaceId").toString());
            optionId = UUID.fromString(optionIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        optionBusinessService.deleteOne(workspaceId, optionId);

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
