package com.sellertool.server.domain.inventory_release.repository;

import com.sellertool.server.domain.inventory_release.entity.InventoryReleaseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryReleaseCustomJdbc {
    void jdbcBulkInsert(List<InventoryReleaseEntity> entities);
}
