package com.sellertool.server.domain.option.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellertool.server.domain.category.entity.QCategoryEntity;
import com.sellertool.server.domain.product.entity.QProductEntity;
import com.sellertool.server.domain.product.entity.QProductEntity_ProductInfoEntity;
import com.sellertool.server.domain.user.entity.QUserEntity;
import com.sellertool.server.domain.workspace.entity.QWorkspaceEntity;
import com.sellertool.server.domain.workspace_member.entity.QWorkspaceMemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
