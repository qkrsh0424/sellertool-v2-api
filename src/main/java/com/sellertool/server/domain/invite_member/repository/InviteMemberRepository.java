package com.sellertool.server.domain.invite_member.repository;

import com.sellertool.server.domain.invite_member.entity.InviteMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface InviteMemberRepository extends JpaRepository<InviteMemberEntity, Integer>, InviteMemberRepositoryCustom {
    List<InviteMemberEntity> findByWorkspaceId(UUID workspaceId);

    @Modifying
    @Query("DELETE FROM InviteMemberEntity i WHERE i.id=:inviteMemberId AND i.workspaceId=:workspaceId")
    void deleteByWorkspaceIdAndInviteMemberId(UUID workspaceId, UUID inviteMemberId);
}
