package com.sellertool.server.domain.product.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.category.entity.QCategoryEntity;
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
public class ProductRepositoryImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QWorkspaceEntity qWorkspaceEntity = QWorkspaceEntity.workspaceEntity;
    private final QWorkspaceMemberEntity qWorkspaceMemberEntity = QWorkspaceMemberEntity.workspaceMemberEntity;
    private final QCategoryEntity qCategoryEntity = QCategoryEntity.categoryEntity;
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductEntity_ProductInfoEntity qProductInfoEntity = QProductEntity_ProductInfoEntity.productInfoEntity;

    @Override
    public List<ProductProjection> qSelectListByWorkspaceIdAndCategoryId(UUID workspaceId, UUID categoryId) {
        JPQLQuery customQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.class,
                                qProductEntity.as("productEntity"),
                                qProductInfoEntity.as("productInfoEntity")
                        )
                )
                .innerJoin(qProductInfoEntity).on(qProductInfoEntity.cid.eq(qProductEntity.productInfoCid))
                .where(qProductEntity.workspaceId.eq(workspaceId))
                .where(qProductEntity.categoryId.eq(categoryId))
                ;

        QueryResults<ProductProjection> results = customQuery.fetchResults();
        return results.getResults();
    }

    @Override
    public Optional<ProductProjection> qSelectM2OJById(UUID productId) {
        JPQLQuery customQuery = query.from(qProductEntity)
                .select(
                        Projections.fields(
                                ProductProjection.class,
                                qProductEntity.as("productEntity"),
                                qProductInfoEntity.as("productInfoEntity")
                        )
                )
                .innerJoin(qProductInfoEntity).on(qProductInfoEntity.id.eq(qProductEntity.id))
                .innerJoin(qCategoryEntity).on(qCategoryEntity.id.eq(qProductEntity.categoryId))
                .where(qProductEntity.id.eq(productId))
                ;

        return Optional.ofNullable((ProductProjection) customQuery.fetchOne());
    }
}
