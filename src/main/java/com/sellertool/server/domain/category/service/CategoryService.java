package com.sellertool.server.domain.category.service;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.category.repository.CategoryRepository;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.utils.FlagInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * @throws NotMatchedFormatException
     */
    public CategoryEntity selectById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(()-> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    /**
     * Soft Delete
     * Dirty Checking Update 방식으로 진행이 되므로, 해당 메소드를 호출하기 전에 반드시 상위 메소드의 @Transactional 유무를 체크를 해줘야한다.
     * @param entity
     */
    public void logicalDeleteOne(CategoryEntity entity){
        entity.setDeletedFlag(FlagInterface.SET_DELETE);
    }
}
