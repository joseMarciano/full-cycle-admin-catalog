package com.fullcyle.admin.catalog.infastructure.category;

import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.infastructure.MySQLGatewayTest;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryJpaEntity;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        final var actualCategory = categoryGateway.create(aCategory);
        assertEquals(1, categoryRepository.count());

        assertEquals(expectedName, aCategory.getName());
        assertEquals(expectedDescription, aCategory.getDescription());
        assertEquals(expectedIsActive, aCategory.isActive());
        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(aCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertNull(aCategory.getDeletedAt());

    }

}
