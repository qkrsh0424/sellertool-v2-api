package com.sellertool.server.domain.inventory_release.repository;

import com.sellertool.server.domain.inventory_release.entity.InventoryReleaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InventoryReleaseJdbcImpl implements InventoryReleaseCustomJdbc {
    private final JdbcTemplate jdbcTemplate;
    private final int DEFAULT_BATCH_SIZE = 300;

    @Override
    public void jdbcBulkInsert(List<InventoryReleaseEntity> entities){
        int batchCount = 0;
        List<InventoryReleaseEntity> subItems = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            subItems.add(entities.get(i));
            if ((i + 1) % DEFAULT_BATCH_SIZE == 0) {
                batchCount = batchInsert(DEFAULT_BATCH_SIZE, batchCount, subItems);
            }
        }
        if (!subItems.isEmpty()) {
            batchCount = batchInsert(DEFAULT_BATCH_SIZE, batchCount, subItems);
        }
//        log.info("batchCount: " + batchCount);
    }

    private int batchInsert(int batchSize, int batchCount, List<InventoryReleaseEntity> subItems){
        String sql = "INSERT INTO inventory_release" +
                "(cid, id, release_unit, memo, created_at, created_by, product_option_cid, product_option_id, erp_order_item_id)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                InventoryReleaseEntity entity = subItems.get(i);
                ps.setObject(1, entity.getCid());
                ps.setObject(2, entity.getId().toString());
                ps.setInt(3, entity.getReleaseUnit());
                ps.setString(4, entity.getMemo());
                ps.setObject(5, entity.getCreatedAt());
                ps.setObject(6, entity.getCreatedBy().toString());
                ps.setLong(7, entity.getOptionCid());
                ps.setObject(8, entity.getOptionId().toString());
                ps.setObject(9, entity.getErpOrderItemId().toString());

            }

            @Override
            public int getBatchSize() {
                return subItems.size();
            }
        });

        subItems.clear();
        batchCount++;
        return batchCount;
    }

}
