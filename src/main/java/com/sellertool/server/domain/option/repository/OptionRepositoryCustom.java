package com.sellertool.server.domain.option.repository;

import com.sellertool.server.domain.option.proj.OptionProjection;
import com.sellertool.server.domain.product.proj.ProductProjection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptionRepositoryCustom {
    List<OptionProjection> qSelectListByWorkspaceIdAndProductId(UUID workspaceId, UUID productId);

    Optional<OptionProjection> qSelectM2OJById(UUID optionId);
}
