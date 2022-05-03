package com.sellertool.server.domain.option.repository;

import com.sellertool.server.domain.option.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionInfoRepository extends JpaRepository<OptionEntity.OptionInfoEntity, Long> {
}
