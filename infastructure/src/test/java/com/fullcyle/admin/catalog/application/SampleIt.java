package com.fullcyle.admin.catalog.application;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIt {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @Test
    public void teste(){
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(repository);
    }
}
