package com.sellertool.server.domain.product.controller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.message.dto.Message;
import com.sellertool.server.domain.product.dto.ProductDto;
import com.sellertool.server.domain.product.service.ProductBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductApi {
    private final ProductBusinessService productBusinessService;

    /**
     * <p>GET URL => /api/v1/products/categories/{categoryId}</p>
     * <p>parameters => [*workspaceId]</p>
     *
     * @param categoryIdObj
     * @param params
     * @param params#workspaceId
     * @return
     */
    @RequiredLogin
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> searchListByCategoryId(
            @PathVariable(value = "categoryId") Object categoryIdObj,
            @RequestParam Map<String, Object> params
    ) {
        Message message = new Message();

        UUID workspaceId = null;
        UUID categoryId = null;

        try {
            workspaceId = UUID.fromString(params.get("workspaceId").toString());
            categoryId = UUID.fromString(categoryIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        message.setData(productBusinessService.searchList(workspaceId, categoryId));

        return new ResponseEntity<>(message, message.getStatus());

    }

    /**
     * <p>POST URL => /api/v1/products/categories/{categoryId}</p>
     * <p>parameters => [*workspaceId]</p>
     *
     * @param categoryIdObj
     * @param params
     * @param params#workspaceId
     * @param productDto
     * @return
     */
    @RequiredLogin
    @PostMapping("/categories/{categoryId}")
    public ResponseEntity<?> createOne(
            @PathVariable(value = "categoryId") Object categoryIdObj,
            @RequestParam Map<String, Object> params,
            @RequestBody ProductDto productDto
    ) {
        Message message = new Message();

        UUID workspaceId = null;
        UUID categoryId = null;

        try {
            workspaceId = UUID.fromString(params.get("workspaceId").toString());
            categoryId = UUID.fromString(categoryIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        productBusinessService.createOne(workspaceId, categoryId, productDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
