package com.sellertool.server.domain.inventory_release.repository;

import com.sellertool.server.domain.inventory_release.entity.InventoryReleaseEntity;
import com.sellertool.server.domain.inventory_release.proj.InventoryReleaseProjection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryReleaseRepositoryCustom {
    Optional<InventoryReleaseProjection> qSelectM2OJByCid(Long cid);
    List<InventoryReleaseProjection> qSelectM2OJList();
    List<InventoryReleaseEntity> qSelectListByCids(List<Long> cids);
    List<InventoryReleaseEntity> qSelectListByOptionCid(Long optionCid);
}
