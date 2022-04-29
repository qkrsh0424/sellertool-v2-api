package com.sellertool.server.domain.category.service;

import com.sellertool.server.domain.category.entity.CategoryEntity;
import com.sellertool.server.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveAndModify(){
        /*
            given
         */
        CategoryEntity entity = Mockito.mock(CategoryEntity.class);

        /*
        when
         */
        categoryService.saveAndModify(entity);

        /*
        then
         */
        Mockito.verify(categoryRepository, Mockito.times(1)).save(entity);
    }
}