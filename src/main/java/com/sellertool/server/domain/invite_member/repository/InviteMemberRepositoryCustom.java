package com.sellertool.server.domain.invite_member.repository;

import com.sellertool.server.domain.invite_member.proj.InviteMemberM2OJProj;
import com.sellertool.server.domain.invite_member.vo.InviteMemberM2OJVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InviteMemberRepositoryCustom {
    List<InviteMemberM2OJProj> qSelectM2OJByWorkspaceId(UUID workspaceId);
}
