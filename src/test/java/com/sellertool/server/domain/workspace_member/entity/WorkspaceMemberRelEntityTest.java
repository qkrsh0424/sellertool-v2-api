package com.sellertool.server.domain.workspace_member.entity;

import com.sellertool.server.domain.workspace_member.repository.WorkspaceMemberRelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
class WorkspaceMemberRelEntityTest {
    @Autowired
    WorkspaceMemberRelRepository workspaceMemberRelRepository;

    @Test
    public void searchTest(){
        workspaceMemberRelRepository.findById(16);
    }
}