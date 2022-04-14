package com.sellertool.server.domain.invite_member.service;

import com.sellertool.server.domain.invite_member.entity.InviteMemberEntity;
import com.sellertool.server.domain.invite_member.proj.InviteMemberM2OJProj;
import com.sellertool.server.domain.invite_member.repository.InviteMemberRepository;
import com.sellertool.server.domain.invite_member.vo.InviteMemberM2OJVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InviteMemberService {
    private final InviteMemberRepository inviteMemberRepository;

    public List<InviteMemberEntity> searchByWorkspaceId(UUID workspaceId) {
        return inviteMemberRepository.findByWorkspaceId(workspaceId);
    }

    public void saveAndModify(InviteMemberEntity inviteMemberEntity) {
        inviteMemberRepository.save(inviteMemberEntity);
    }

    public List<InviteMemberM2OJProj> qSelectM2OJByWorkspaceId(UUID workspaceId) {
        List<InviteMemberM2OJProj> proj = inviteMemberRepository.qSelectM2OJByWorkspaceId(workspaceId);
        return proj;
    }

    public void deleteByWorkspaceIdAndInviteMemberId(UUID workspaceId, UUID inviteMemberId) {
        inviteMemberRepository.deleteByWorkspaceIdAndInviteMemberId(workspaceId, inviteMemberId);
    }
}
