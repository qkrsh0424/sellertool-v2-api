package com.sellertool.server.domain.option.repository;

import com.sellertool.server.domain.option.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long>, OptionRepositoryCustom {
    Optional<OptionEntity> findById(UUID optionId);

    @Query(value="SELECT po.cid AS cid, \n" +
            "(SELECT SUM(prl.release_unit) FROM product_release prl WHERE po.cid=prl.product_option_cid) AS releasedSum, \n" +
            "(SELECT SUM(prc.receive_unit) FROM product_receive prc WHERE po.cid=prc.product_option_cid) AS receivedSum \n" +
            "FROM product_option po WHERE po.cid IN :optionCids", nativeQuery = true)
    List<Tuple> searchReceivedAndReleasedUnitSumTuplesByOptionCids(List<Long> optionCids);
}
