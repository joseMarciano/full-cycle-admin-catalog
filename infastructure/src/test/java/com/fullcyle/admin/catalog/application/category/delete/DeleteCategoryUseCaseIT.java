package com.fullcyle.admin.catalog.application.category.delete;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryJpaEntity;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DefaultDeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = Category.newCategory(
                "filmes",
                "Most watched",
                true
        );

        Assertions.assertEquals(0, repository.count());
        save(aCategory);
        Assertions.assertEquals(1, repository.count());

        final var expectedId = aCategory.getId();

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, repository.count());

    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedId = CategoryID.from("123");
        Assertions.assertEquals(0, repository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, repository.count());

    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnException() {
        final var aCategory = Category.newCategory(
                "filmes",
                "Most watched",
                true
        );

        final var expectedId = aCategory.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(eq(expectedId));

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));

    }


    private void save(final Category... categories) {
        List<CategoryJpaEntity> categoriesJpaEntities = Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList();

        repository.saveAllAndFlush(categoriesJpaEntities);
    }
}
