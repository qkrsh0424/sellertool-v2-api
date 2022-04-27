package com.sellertool.server.domain.category.controller;

import com.sellertool.server.annotation.RequiredLogin;
import com.sellertool.server.domain.category.dto.CategoryDto;
import com.sellertool.server.domain.category.service.CategoryBusinessService;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.message.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryApi {
    private final CategoryBusinessService categoryBusinessService;

    /**
     * URL => /api/v1/category/workspaces/{workspaceId}
     * @param workspaceIdObj
     * @return
     */
    @GetMapping("/workspaces/{workspaceId}")
    @RequiredLogin
    public ResponseEntity<?> searchList(@PathVariable(value = "workspaceId") Object workspaceIdObj) {
        Message message = new Message();

        UUID workspaceId = null;
        try {
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        message.setData(categoryBusinessService.searchList(workspaceId));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");
        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * URL => /api/v1/category/workspaces/{workspaceId}
     * @param workspaceIdObj
     * @param categoryDto
     * @return
     */
    @PostMapping("/workspaces/{workspaceId}")
    @RequiredLogin
    public ResponseEntity<?> createOne(@PathVariable(value = "workspaceId") Object workspaceIdObj, @RequestBody CategoryDto categoryDto) {
        Message message = new Message();

        UUID workspaceId = null;
        try {
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        categoryBusinessService.createOne(workspaceId, categoryDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    /**
     * URL => /api/v1/category/workspaces/{workspaceId}
     * @param workspaceIdObj
     * @param categoryDto
     * @return
     */
    @PutMapping("/workspaces/{workspaceId}")
    @RequiredLogin
    public ResponseEntity<?> updateOne(@PathVariable(value = "workspaceId") Object workspaceIdObj, @RequestBody CategoryDto categoryDto) {
        Message message = new Message();

        UUID workspaceId = null;
        try {
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        categoryBusinessService.updateOne(workspaceId, categoryDto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @DeleteMapping("/{categoryId}/workspaces/{workspaceId}")
    @RequiredLogin
    public ResponseEntity<?> deleteOne(@PathVariable(value = "categoryId") Object categoryIdObj, @PathVariable(value = "workspaceId") Object workspaceIdObj){
        Message message = new Message();

        UUID workspaceId = null;
        UUID categoryId = null;
        try {
            workspaceId = UUID.fromString(workspaceIdObj.toString());
            categoryId = UUID.fromString(categoryIdObj.toString());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NotMatchedFormatException("잘못된 요청입니다. 정상적인 요청을 이용해 주세요.");
        }

        categoryBusinessService.deleteOne(workspaceId, categoryId);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
