package com.sellertool.server.domain.option.service;

import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.option.entity.OptionEntity;
import com.sellertool.server.domain.option.proj.OptionProjection;
import com.sellertool.server.domain.option.repository.OptionRepository;
import com.sellertool.server.domain.product.entity.ProductEntity;
import com.sellertool.server.domain.product.proj.ProductProjection;
import com.sellertool.server.utils.FlagInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepository optionRepository;

    public void saveAndModify(OptionEntity entity){
        optionRepository.save(entity);
    }

    public List<OptionProjection> qSearchListByWorkspaceIdAndProductId(UUID workspaceId, UUID productId) {
        return optionRepository.qSelectListByWorkspaceIdAndProductId(workspaceId, productId);
    }

    public OptionEntity searchById(UUID optionId) {
        return optionRepository.findById(optionId).orElseThrow(()-> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    /**
     * Soft Delete
     * Dirty Checking Update 방식으로 진행이 되므로, 해당 메소드를 호출하기 전에 반드시 상위 메소드의 @Transactional 유무를 체크를 해줘야한다.
     */
    public void logicalDeleteOne(OptionEntity entity) {
        entity.setDeletedFlag(FlagInterface.SET_DELETE);
    }

    public OptionProjection qSearchM2OJById(UUID optionId){
        return optionRepository.qSelectM2OJById(optionId).orElseThrow(() -> new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }
}
