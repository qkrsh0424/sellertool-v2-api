package com.sellertool.server.domain.category.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.category.entity.QCategoryEntity;
import com.sellertool.server.domain.category.proj.CategoryM2OJProj;
import com.sellertool.server.domain.user.entity.QUserEntity;
import com.sellertool.server.domain.workspace.entity.QWorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.QWorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QWorkspaceEntity qWorkspaceEntity = QWorkspaceEntity.workspaceEntity;
    private final QWorkspaceMemberEntity qWorkspaceMemberEntity = QWorkspaceMemberEntity.workspaceMemberEntity;
    private final QCategoryEntity qCategoryEntity = QCategoryEntity.categoryEntity;

    @Override
    public Optional<CategoryM2OJProj> qSelectM2OJ(UUID categoryId, UUID userId) {
        JPQLQuery customQuery = query.from(qCategoryEntity)
                .select(
                        Projections.fields(CategoryM2OJProj.class,
                                qCategoryEntity.as("categoryEntity"),
                                qWorkspaceMemberEntity.as("workspaceMemberEntity"),
                                qWorkspaceEntity.as("workspaceEntity"),
                                qUserEntity.as("userEntity")
                        )
                )
                .innerJoin(qWorkspaceEntity).on(qWorkspaceEntity.id.eq(qCategoryEntity.workspaceId))
                .innerJoin(qWorkspaceMemberEntity).on(qWorkspaceMemberEntity.workspaceId.eq(qWorkspaceEntity.id))
                .innerJoin(qUserEntity).on(qUserEntity.id.eq(qWorkspaceMemberEntity.userId))
                .where(qCategoryEntity.id.eq(categoryId))
                .where(qUserEntity.id.eq(userId));
        return Optional.ofNullable((CategoryM2OJProj) customQuery.fetchOne());
    }
}
