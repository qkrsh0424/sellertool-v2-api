package com.sellertool.server.domain.inventory_release.service;

import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.inventory_release.entity.InventoryReleaseEntity;
import com.sellertool.server.domain.inventory_release.proj.InventoryReleaseProjection;
import com.sellertool.server.domain.inventory_release.repository.InventoryReleaseCustomJdbc;
import com.sellertool.server.domain.inventory_release.repository.InventoryReleaseRepository;
import com.sellertool.server.domain.option.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryReleaseService {
    private final InventoryReleaseRepository inventoryReleaseRepository;
    private final OptionService optionService;
    private final InventoryReleaseCustomJdbc inventoryReleaseCustomJdbc;

    public InventoryReleaseEntity searchOne(Long inventoryReleaseCid){
        return inventoryReleaseRepository.findById(inventoryReleaseCid).orElseThrow(()->new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    public InventoryReleaseProjection qSearchM2OJByCid(Long cid){
        return inventoryReleaseRepository.qSelectM2OJByCid(cid).orElseThrow(()->new NotMatchedFormatException("해당 데이터를 찾을 수 없습니다."));
    }

    public List<InventoryReleaseEntity> searchList(){
        return inventoryReleaseRepository.findAll();
    }

    public List<InventoryReleaseEntity> qSearchListByOptionCid(Long optionCid){
        return inventoryReleaseRepository.qSelectListByOptionCid(optionCid);
    }
}
