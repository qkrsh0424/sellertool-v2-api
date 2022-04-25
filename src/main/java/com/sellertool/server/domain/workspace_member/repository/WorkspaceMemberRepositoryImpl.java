package com.sellertool.server.domain.workspace_member.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.user.entity.QUserEntity;
import com.sellertool.server.domain.user.entity.UserEntity;
import com.sellertool.server.domain.workspace.entity.QWorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.QWorkspaceMemberEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WorkspaceMemberRepositoryImpl implements WorkspaceMemberRepositoryCustom {
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QWorkspaceEntity qWorkspaceEntity = QWorkspaceEntity.workspaceEntity;
    private final QWorkspaceMemberEntity qWorkspaceMemberEntity = QWorkspaceMemberEntity.workspaceMemberEntity;


    @Override
    public List<WorkspaceMemberM2OJProj> qSelectM2OJByWorkspaceId(UUID workspaceId) {
        JPQLQuery customQuery = query.from(qWorkspaceMemberEntity)
                .select(
                        Projections.fields(WorkspaceMemberM2OJProj.class,
                                qWorkspaceMemberEntity.as("workspaceMemberEntity"),
                                qWorkspaceEntity.as("workspaceEntity"),
                                qUserEntity.as("userEntity")
                        )
                )
                .join(qWorkspaceEntity).on(qWorkspaceEntity.id.eq(qWorkspaceMemberEntity.workspaceId))
                .join(qUserEntity).on(qUserEntity.id.eq(qWorkspaceMemberEntity.userId))
                .where(qWorkspaceMemberEntity.workspaceId.eq(workspaceId));

        QueryResults<WorkspaceMemberM2OJProj> result = customQuery.fetchResults();

        return result.getResults();
    }

    @Override
    public Optional<WorkspaceMemberM2OJProj> qSelectM2OJ(UUID workspaceMemberId) {
        JPQLQuery customQuery = query.from(qWorkspaceMemberEntity)
                .select(
                        Projections.fields(WorkspaceMemberM2OJProj.class,
                                qWorkspaceMemberEntity.as("workspaceMemberEntity"),
                                qWorkspaceEntity.as("workspaceEntity"),
                                qUserEntity.as("userEntity")
                        )
                )
                .innerJoin(qWorkspaceEntity).on(qWorkspaceEntity.id.eq(qWorkspaceMemberEntity.workspaceId))
                .innerJoin(qUserEntity).on(qUserEntity.id.eq(qWorkspaceMemberEntity.userId))
                .where(qWorkspaceMemberEntity.id.eq(workspaceMemberId));
        return Optional.ofNullable((WorkspaceMemberM2OJProj) customQuery.fetchOne());
    }
}
