package com.sellertool.server.domain.category.service;

import com.sellertool.server.domain.category.dto.CategoryDto;
import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.user.service.UserService;
import com.sellertool.server.domain.workspace.entity.WorkspaceEntity;
import com.sellertool.server.domain.workspace_member.proj.WorkspaceMemberM2OJProj;
import com.sellertool.server.domain.workspace_member.service.WorkspaceMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
class CategoryBusinessServiceTest {
    @Mock
    CategoryService categoryService;

    @Mock
    UserService userService;

    @Mock
    WorkspaceMemberService workspaceMemberService;

    @InjectMocks
    CategoryBusinessService categoryBusinessService;

    private static MockedStatic<WorkspaceEntity> mockedWorkspaceEntity;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @BeforeTestClass
    public static void beforeClass(){
        mockedWorkspaceEntity = Mockito.mockStatic(WorkspaceEntity.class);
    }

    @AfterTestClass
    public static void afterClass(){
        mockedWorkspaceEntity.close();
    }

    @Test
    public void createOne(){
    }
}