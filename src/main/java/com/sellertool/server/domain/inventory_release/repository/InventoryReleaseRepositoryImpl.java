package com.sellertool.server.domain.inventory_release.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.category.entity.QCategoryEntity;
import com.sellertool.server.domain.inventory_release.entity.InventoryReleaseEntity;
import com.sellertool.server.domain.inventory_release.entity.QInventoryReleaseEntity;
import com.sellertool.server.domain.inventory_release.proj.InventoryReleaseProjection;
import com.sellertool.server.domain.option.entity.QOptionEntity;
import com.sellertool.server.domain.option.entity.QOptionEntity_OptionInfoEntity;
import com.sellertool.server.domain.product.entity.QProductEntity;
import com.sellertool.server.domain.product.entity.QProductEntity_ProductInfoEntity;
import com.sellertool.server.domain.product.proj.ProductProjection;
import com.sellertool.server.domain.user.entity.QUserEntity;
import com.sellertool.server.domain.workspace.entity.QWorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.QWorkspaceMemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InventoryReleaseRepositoryImpl implements InventoryReleaseRepositoryCustom {
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QWorkspaceEntity qWorkspaceEntity = QWorkspaceEntity.workspaceEntity;
    private final QWorkspaceMemberEntity qWorkspaceMemberEntity = QWorkspaceMemberEntity.workspaceMemberEntity;
    private final QCategoryEntity qCategoryEntity = QCategoryEntity.categoryEntity;
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductEntity_ProductInfoEntity qProductInfoEntity = QProductEntity_ProductInfoEntity.productInfoEntity;
    private final QOptionEntity qOptionEntity = QOptionEntity.optionEntity;
    private final QOptionEntity_OptionInfoEntity qOptionInfoEntity = QOptionEntity_OptionInfoEntity.optionInfoEntity;
    private final QInventoryReleaseEntity qInventoryReleaseEntity = QInventoryReleaseEntity.inventoryReleaseEntity;

    @Override
    public Optional<InventoryReleaseProjection> qSelectM2OJByCid(Long cid) {
        JPQLQuery customQuery = query.from(qInventoryReleaseEntity)
                .select(
                        Projections.fields(
                                InventoryReleaseProjection.class,
                                qInventoryReleaseEntity.as("inventoryReleaseEntity"),
                                qCategoryEntity.as("categoryEntity"),
                                qProductEntity.as("productEntity"),
                                qOptionEntity.as("optionEntity"),
                                qUserEntity.as("userEntity")
                        )
                )
                .innerJoin(qOptionEntity).on(qOptionEntity.cid.eq(qInventoryReleaseEntity.optionCid))
                .innerJoin(qProductEntity).on(qProductEntity.cid.eq(qOptionEntity.productCid))
                .innerJoin(qCategoryEntity).on(qCategoryEntity.cid.eq(qProductEntity.categoryCid))
                .innerJoin(qUserEntity).on(qUserEntity.id.eq(qInventoryReleaseEntity.createdBy))
                .where(qInventoryReleaseEntity.cid.eq(cid));
        return Optional.ofNullable((InventoryReleaseProjection) customQuery.fetchOne());
    }

    @Override
    public List<InventoryReleaseProjection> qSelectM2OJList() {
        JPQLQuery customQuery = query.from(qInventoryReleaseEntity)
                .select(
                        Projections.fields(
                                InventoryReleaseProjection.class,
                                qInventoryReleaseEntity.as("inventoryReleaseEntity"),
                                qCategoryEntity.as("categoryEntity"),
                                qProductEntity.as("productEntity"),
                                qOptionEntity.as("optionEntity"),
                                qUserEntity.as("userEntity")
                        )
                )
                .innerJoin(qOptionEntity).on(qOptionEntity.cid.eq(qInventoryReleaseEntity.optionCid))
                .innerJoin(qProductEntity).on(qProductEntity.cid.eq(qOptionEntity.productCid))
                .innerJoin(qCategoryEntity).on(qCategoryEntity.cid.eq(qProductEntity.categoryCid))
                .innerJoin(qUserEntity).on(qUserEntity.id.eq(qInventoryReleaseEntity.createdBy));
        QueryResults<InventoryReleaseProjection> results = customQuery.fetchResults();
        return results.getResults();
    }

    @Override
    public List<InventoryReleaseEntity> qSelectListByCids(List<Long> cids) {
        JPQLQuery customQuery = query.from(qInventoryReleaseEntity)
                .select(qInventoryReleaseEntity)
                .where(qInventoryReleaseEntity.cid.in(cids));

        QueryResults<InventoryReleaseEntity> results = customQuery.fetchResults();
        return results.getResults();
    }

    @Override
    public List<InventoryReleaseEntity> qSelectListByOptionCid(Long optionCid) {
        JPQLQuery customQuery = query.from(qInventoryReleaseEntity)
                .select(qInventoryReleaseEntity)
                .where(qInventoryReleaseEntity.optionCid.eq(optionCid));

        QueryResults<InventoryReleaseEntity> results = customQuery.fetchResults();
        return results.getResults();
    }
}
