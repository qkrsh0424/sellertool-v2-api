package com.sellertool.server.domain.margin_record.service;

import com.sellertool.server.domain.margin_record.entity.MarginRecordEntity;
import com.sellertool.server.domain.margin_record.repository.MarginRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MarginRecordService {
    private final MarginRecordRepository marginRecordRepository;

    @Autowired
    public MarginRecordService(
            MarginRecordRepository marginRecordRepository
    ) {
        this.marginRecordRepository = marginRecordRepository;
    }

    public MarginRecordEntity saveAndGet(MarginRecordEntity entity) {
        return marginRecordRepository.save(entity);
    }

    public List<MarginRecordEntity> searchList(Map<String, Object> params) {
        return marginRecordRepository.qSelectList(params);
    }

    public void saveAndModify(MarginRecordEntity entity) {
        marginRecordRepository.save(entity);
    }

    public void deleteOne(Integer cid) {
        marginRecordRepository.deleteById(cid);
    }
}
