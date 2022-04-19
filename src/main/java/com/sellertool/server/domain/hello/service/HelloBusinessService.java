package com.sellertool.server.domain.hello.service;

import com.sellertool.server.annotation.WorkspacePermission;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HelloBusinessService {
    @WorkspacePermission(
            MasterOnly = true
    )
    public void hello1(UUID workspaceId){

    }
}
