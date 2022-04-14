package com.sellertool.server.domain.margin_record.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.margin_record.entity.MarginRecordEntity;
import com.sellertool.server.domain.margin_record.entity.QMarginRecordEntity;
import com.sellertool.server.domain.user.entity.QUserEntity;
import com.sellertool.server.domain.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class MarginRecordRepositoryImpl implements MarginRecordRepositoryCustom{
    private final JPAQueryFactory query;

    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final QMarginRecordEntity qMarginRecordEntity = QMarginRecordEntity.marginRecordEntity;


    @Autowired
    public MarginRecordRepositoryImpl(
            JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<MarginRecordEntity> qSelectList(Map<String, Object> params) {
        JPQLQuery customQuery = query.from(qMarginRecordEntity)
                .select(qMarginRecordEntity)
                .where(eqId(params))
                .where(eqWorkspaceId(params))
                ;

        QueryResults<MarginRecordEntity> result = customQuery.fetchResults();
        return result.getResults();
    }

    private BooleanExpression eqId(Map<String, Object> params){
        Object idObj = params.get("marginRecordId");
        UUID marginRecordId = null;

        if(idObj == null){
            return null;
        }

        try{
            marginRecordId = UUID.fromString(idObj.toString());
        } catch (IllegalArgumentException e){
            throw new NotMatchedFormatException("정상적인 요청이 아닙니다.");
        }
        return qMarginRecordEntity.id.eq(marginRecordId);
    }

    private BooleanExpression eqWorkspaceId(Map<String, Object> params){
        Object workspaceIdObj = params.get("workspaceId");
        UUID workspaceId = null;

        if(workspaceIdObj == null){
            return null;
        }

        try{
            workspaceId = UUID.fromString(workspaceIdObj.toString());
        } catch (IllegalArgumentException e){
            throw new NotMatchedFormatException("정상적인 요청이 아닙니다.");
        }
        return qMarginRecordEntity.workspaceId.eq(workspaceId);
    }
}
