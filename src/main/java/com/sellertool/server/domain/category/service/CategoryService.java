package com.sellertool.server.domain.category.service;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.category.repository.CategoryRepository;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryEntity saveAndModify(CategoryEntity entity) {
        return categoryRepository.save(entity);
    }

    public List<CategoryEntity> selectListByWorkspaceId(UUID workspaceId) {
        return categoryRepository.findAllByWorkspaceId(workspaceId);
    }

    public CategoryEntity selectById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(()-> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

}
