package com.sellertool.server.domain.option.service;

import com.sellertool.server.domain.option.entity.OptionEntity;
import com.sellertool.server.domain.option.repository.OptionInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionInfoService {
    private final OptionInfoRepository optionInfoRepository;

    public OptionEntity.OptionInfoEntity saveAndGet(OptionEntity.OptionInfoEntity entity){
        return optionInfoRepository.save(entity);
    }
}
