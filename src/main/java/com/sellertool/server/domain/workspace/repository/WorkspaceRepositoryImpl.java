package com.sellertool.server.domain.workspace.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.user.entity.QUserEntity;
import com.sellertool.server.domain.workspace.entity.QWorkspaceEntity;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace.utils.WorkspaceStaticVariable;
import com.sellertool.server.domain.workspace_member.entity.QWorkspaceMemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class WorkspaceRepositoryImpl implements WorkspaceRepositoryCustom {
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QWorkspaceEntity qWorkspaceEntity = QWorkspaceEntity.workspaceEntity;
    private final QWorkspaceMemberEntity qWorkspaceMemberEntity = QWorkspaceMemberEntity.workspaceMemberEntity;

    @Autowired
    public WorkspaceRepositoryImpl(
            JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<WorkspaceEntity> qSelectOwnWorkspace(UUID userId) {
        JPQLQuery customQuery = query.from(qWorkspaceEntity)
                .select(qWorkspaceEntity)
                .where(qWorkspaceEntity.masterId.eq(userId))
                .where(qWorkspaceEntity.publicYn.eq(WorkspaceStaticVariable.PUBLIC_N))
                .where(qWorkspaceEntity.deleteProtectionYn.eq(WorkspaceStaticVariable.DELETE_PROTECTION_Y));
        QueryResults<WorkspaceEntity> result = customQuery.fetchResults();

        return result.getResults();
    }

    @Override
    public List<WorkspaceEntity> qSelectList(UUID workspaceId) {
        JPQLQuery customQuery = query.from(qWorkspaceEntity)
                .select(qWorkspaceEntity)
                .where(qWorkspaceEntity.id.eq(workspaceId));

        QueryResults<WorkspaceEntity> result = customQuery.fetchResults();
        return result.getResults();
    }

    @Override
    public List<WorkspaceEntity> qSelectListByUserId(UUID userId) {
        JPQLQuery customQuery = query.from(qWorkspaceMemberEntity)
                .select(qWorkspaceEntity)
                .join(qWorkspaceEntity).on(qWorkspaceEntity.id.eq(qWorkspaceMemberEntity.workspaceId))
                .where(qWorkspaceMemberEntity.userId.eq(userId));

        QueryResults<WorkspaceEntity> result = customQuery.fetchResults();
        return result.getResults();
    }
}
