package com.sellertool.server.domain.category.vo;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryVo {
    private Integer cid;
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private UUID workspaceId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BasicResponse{
        private UUID id;
        private String name;
        private UUID workspaceId;

        public static CategoryVo.BasicResponse toVo(CategoryEntity entity){
            CategoryVo.BasicResponse vo = BasicResponse.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .workspaceId(entity.getWorkspaceId())
                    .build();
            return vo;
        }
    }
}
