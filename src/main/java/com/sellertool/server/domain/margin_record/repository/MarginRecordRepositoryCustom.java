package com.sellertool.server.domain.margin_record.repository;

import com.sellertool.server.domain.margin_record.entity.MarginRecordEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MarginRecordRepositoryCustom {
    List<MarginRecordEntity> qSelectList(Map<String, Object> params);
}
