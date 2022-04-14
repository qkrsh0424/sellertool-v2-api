package com.sellertool.server.domain.margin_record.repository;

import com.sellertool.server.domain.margin_record.entity.MarginRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarginRecordRepository extends JpaRepository<MarginRecordEntity, Integer>, MarginRecordRepositoryCustom {
}
