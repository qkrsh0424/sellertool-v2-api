package com.sellertool.server.domain.erp_order_item.service;

import com.sellertool.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.sellertool.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.sellertool.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.sellertool.server.domain.erp_order_item.repository.ErpOrderItemCustomJdbc;
import com.sellertool.server.domain.erp_order_item.repository.ErpOrderItemRepository;
import com.sellertool.server.domain.exception.dto.CustomNotFoundDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ErpOrderItemService {
    private final ErpOrderItemRepository erpOrderItemRepository;
    private final ErpOrderItemCustomJdbc erpOrderItemCustomJdbc;

    /**
     * <b>DB Insert Or Update Related Method</b>
     * <p>
     * 피아르 엑셀 데이터를 저장 or 수정한다.
     *
     * @param itemEntity : ErpOrderItemEntity
     * @see ErpOrderItemRepository#save
     */
    public void saveAndModify(ErpOrderItemEntity itemEntity) {
        erpOrderItemRepository.save(itemEntity);
    }

    /**
     * <b>DB Insert Or Update Related Method</b>
     * <p>
     * 피아르 엑셀 데이터를 저장 or 수정한다.
     *
     * @param itemEntities : List::ErpOrderItemEntity::
     * @see ErpOrderItemRepository#saveAll
     */
    @Transactional
    public void saveListAndModify(List<ErpOrderItemEntity> itemEntities) {
        erpOrderItemRepository.saveAll(itemEntities);
    }

    @Transactional
    public void bulkInsert(List<ErpOrderItemEntity> itemEntities){
        erpOrderItemCustomJdbc.jdbcBulkInsert(itemEntities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 피아르 엑셀 데이터를 모두 조회한다.
     *
     * @return List::ErpOrderItemProj::
     * @see ErpOrderItemRepository#qfindAllM2OJ
     */
    public List<ErpOrderItemProj> findAllM2OJ(Map<String, Object> params) {
        return erpOrderItemRepository.qfindAllM2OJ(params);
    }

    public List<ErpOrderItemProj> findAllM2OJ(List<UUID> ids, Map<String, Object> params) {
        return erpOrderItemRepository.qfindAllM2OJByIdList(ids, params);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 페이지 처리 후 피아르 엑셀 데이터를 모두 조회한다.
     *
     * @return List::ErpOrderItemProj::
     * @see ErpOrderItemRepository#qfindAllM2OJ
     */
    public Page<ErpOrderItemProj> findAllM2OJByPage(Map<String, Object> params, Pageable pageable) {
        return erpOrderItemRepository.qfindAllM2OJByPage(params, pageable);
    }

    public Page<ErpOrderItemProj> findReleaseItemM2OJByPage(Map<String, Object> params, Pageable pageable) {
        return erpOrderItemRepository.qfindReleaseItemM2OJByPage(params, pageable);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * id 값들과 대응하는 엑셀 데이터를 모두 조회한다.
     *
     * @param idList : List::UUID::
     * @return List::ErpOrderItemEntity::
     * @see ErpOrderItemRepository#qfindAllByIdList
     */
    public List<ErpOrderItemEntity> findAllByIdList(List<UUID> idList) {
        return erpOrderItemRepository.qfindAllByIdList(idList);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * id 값에 대응하는 엑셀 데이터를 삭제한다.
     *
     * @param id : UUID
     * @ErpOrderItemRepository#findById
     * @ErpOrderItemRepository#delete
     */
    public void delete(UUID id) {
        erpOrderItemRepository.findById(id).ifPresent(item -> {
            erpOrderItemRepository.delete(item);
        });
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * id 값에 대응하는 엑셀 데이터를 모두 삭제한다.
     *
     * @param ids : List::UUID::
     * @ErpOrderItemRepository#deleteAllById
     */
    public void deleteBatch(List<UUID> ids) {
        erpOrderItemRepository.deleteAllById(ids);
    }

    public ErpOrderItemEntity searchOne(UUID id) {
        Optional<ErpOrderItemEntity> entityOpt = erpOrderItemRepository.findById(id);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    public List<ErpOrderItemEntity> findDuplicationItems(List<String> orderNumber1, List<String> receiver, List<String> prodName, List<String> optionName, List<Integer> unit) {
        return erpOrderItemRepository.findDuplicationItems(orderNumber1, receiver, prodName, optionName, unit);
    }

    public List<ErpOrderItemEntity> getEntities(List<ErpOrderItemDto> itemDtos) {
        List<UUID> ids = itemDtos.stream().map(r -> r.getId()).collect(Collectors.toList());
        return erpOrderItemRepository.qfindAllByIdList(ids);

    }
}
