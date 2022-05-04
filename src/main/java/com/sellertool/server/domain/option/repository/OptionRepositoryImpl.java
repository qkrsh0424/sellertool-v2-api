package com.sellertool.server.domain.option.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.category.entity.QCategoryEntity;
import com.sellertool.server.domain.option.entity.QOptionEntity;
import com.sellertool.server.domain.option.entity.QOptionEntity_OptionInfoEntity;
import com.sellertool.server.domain.option.proj.OptionProjection;
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
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OptionRepositoryImpl implements OptionRepositoryCustom{
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QWorkspaceEntity qWorkspaceEntity = QWorkspaceEntity.workspaceEntity;
    private final QWorkspaceMemberEntity qWorkspaceMemberEntity = QWorkspaceMemberEntity.workspaceMemberEntity;
    private final QCategoryEntity qCategoryEntity = QCategoryEntity.categoryEntity;
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductEntity_ProductInfoEntity qProductInfoEntity = QProductEntity_ProductInfoEntity.productInfoEntity;
    private final QOptionEntity qOptionEntity = QOptionEntity.optionEntity;
    private final QOptionEntity_OptionInfoEntity qOptionInfoEntity = QOptionEntity_OptionInfoEntity.optionInfoEntity;

    @Override
    public List<OptionProjection> qSelectListByWorkspaceIdAndProductId(UUID workspaceId, UUID productId) {
        JPQLQuery customQuery = query.from(qOptionEntity)
                .select(
                        Projections.fields(
                                OptionProjection.class,
                                qOptionEntity.as("optionEntity"),
                                qOptionInfoEntity.as("optionInfoEntity")
                        )
                )
                .innerJoin(qOptionInfoEntity).on(qOptionInfoEntity.cid.eq(qOptionEntity.optionInfoCid))
                .where(qOptionEntity.workspaceId.eq(workspaceId))
                .where(qOptionEntity.productId.eq(productId))
                ;

        QueryResults<OptionProjection> results = customQuery.fetchResults();
        return results.getResults();
    }

    @Override
    public Optional<OptionProjection> qSelectM2OJById(UUID optionId) {
        JPQLQuery customQuery = query.from(qOptionEntity)
                .select(
                        Projections.fields(
                                OptionProjection.class,
                                qOptionEntity.as("optionEntity"),
                                qOptionInfoEntity.as("optionInfoEntity")
                        )
                )
                .innerJoin(qOptionInfoEntity).on(qOptionInfoEntity.cid.eq(qOptionEntity.optionInfoCid))
                .where(qOptionEntity.id.eq(optionId))
                ;
        return Optional.ofNullable((OptionProjection) customQuery.fetchOne());
    }
}
