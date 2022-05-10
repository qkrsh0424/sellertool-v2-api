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

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public void setReceivedAndReleasedAndStockSum(List<OptionEntity> optionEntities) {
        List<Long> productOptionCids = optionEntities.stream().map(r -> r.getCid()).collect(Collectors.toList());

        List<Tuple> stockUnitTuple = optionRepository.searchReceivedAndReleasedUnitSumTuplesByOptionCids(productOptionCids);
        stockUnitTuple.forEach(r -> {
            Integer cid = r.get("cid", Integer.class);
            Integer receivedSum = r.get("receivedSum", BigDecimal.class) != null ? r.get("receivedSum", BigDecimal.class).intValue() : 0;
            Integer releasedSum = r.get("releasedSum", BigDecimal.class) != null ? r.get("releasedSum", BigDecimal.class).intValue() : 0;

            optionEntities.forEach(entity -> {
                if(entity.getCid().equals(cid)){
                    entity.setReceivedSum(receivedSum);
                    entity.setReleasedSum(releasedSum);
                    entity.setStockSumUnit(receivedSum - releasedSum);
                }
            });
        });
    }
}
