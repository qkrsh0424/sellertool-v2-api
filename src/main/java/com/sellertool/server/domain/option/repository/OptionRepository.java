package com.sellertool.server.domain.option.repository;

import com.sellertool.server.domain.option.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long>, OptionRepositoryCustom {
    Optional<OptionEntity> findById(UUID optionId);
}
