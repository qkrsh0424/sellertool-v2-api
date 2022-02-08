package com.sellertool.server.domain.margin_record.service;

import com.sellertool.server.domain.exception.dto.InvalidUserAuthException;
import com.sellertool.server.domain.exception.dto.NotMatchedFormatException;
import com.sellertool.server.domain.margin_record.dto.MarginRecordDto;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MarginRecordBusinessService {
    private final UserService userService;
    private final WorkspaceMemberService workspaceMemberService;

    @Autowired
    public MarginRecordBusinessService(
            UserService userService,
            WorkspaceMemberService workspaceMemberService
    ) {
        this.userService = userService;
        this.workspaceMemberService = workspaceMemberService;
    }

    public void createOne(MarginRecordDto marginRecordDto) {
        if (!userService.isLogin()) {
            throw new InvalidUserAuthException("로그인이 필요한 서비스 입니다.");
        }

        UUID USER_ID = userService.getUserId();
        UUID MARGIN_RECORD_ID = UUID.randomUUID();
        UUID WORKSPACE_ID = marginRecordDto.getWorkspaceId();
        System.out.println(marginRecordDto);
        if(WORKSPACE_ID == null){
            throw new NotMatchedFormatException("워크스페이스를 지정해주세요.");
        }

        System.out.println(workspaceMemberService.isAccessedWritePermissionOfWorkspace(WORKSPACE_ID, USER_ID));
    }
}
