package com.sellertool.server.domain.invite_member.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.invite_member.entity.QInviteMemberEntity;
import com.sellertool.server.domain.invite_member.proj.InviteMemberM2OJProj;
import com.sellertool.server.domain.user.entity.QUserEntity;
import com.sellertool.server.domain.workspace.entity.QWorkspaceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InviteMemberRepositoryImpl implements InviteMemberRepositoryCustom {
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QWorkspaceEntity qWorkspaceEntity = QWorkspaceEntity.workspaceEntity;
    private final QInviteMemberEntity qInviteMemberEntity = QInviteMemberEntity.inviteMemberEntity;

    @Override
    public List<InviteMemberM2OJProj> qSelectM2OJByWorkspaceId(UUID workspaceId) {
        JPQLQuery customQuery = query.from(qInviteMemberEntity)
                .select(
                        Projections.fields(
                                InviteMemberM2OJProj.class,
                                qInviteMemberEntity.as("inviteMemberEntity"),
                                qUserEntity.as("userEntity"),
                                qWorkspaceEntity.as("workspaceEntity")
                        )
                )
                .join(qUserEntity).on(qUserEntity.id.eq(qInviteMemberEntity.userId))
                .join(qWorkspaceEntity).on(qWorkspaceEntity.id.eq(qInviteMemberEntity.workspaceId))
                .where(qInviteMemberEntity.workspaceId.eq(workspaceId))
                ;

        QueryResults<InviteMemberM2OJProj> results = customQuery.fetchResults();
        return results.getResults();
    }
}
