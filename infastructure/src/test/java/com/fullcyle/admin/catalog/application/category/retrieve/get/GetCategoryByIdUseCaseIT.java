package com.fullcyle.admin.catalog.application.category.retrieve.get;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.DomainException;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryJpaEntity;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedActive
        );

        final var expectedId = aCategory.getId();

        save(aCategory);

        final var actualCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(actualCategory.deletedAt(), aCategory.getDeletedAt());
        Assertions.assertEquals(actualCategory.updatedAt(), aCategory.getUpdatedAt().truncatedTo(ChronoUnit.MICROS));
        Assertions.assertEquals(actualCategory.createdAt(), aCategory.getCreatedAt().truncatedTo(ChronoUnit.MICROS));

    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedErrorMessage = "Category with ID any_id was not found";
        final var expectedId = CategoryID.from("any_id");

        doReturn(Optional.empty())
                .when(categoryGateway).findById(eq(expectedId));

        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedActive
        );

        final var expectedId = aCategory.getId();

        Assertions.assertEquals(0, repository.count());

        save(aCategory);

        Assertions.assertEquals(1, repository.count());

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway)
                .findById(eq(expectedId));

        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... categories) {
        List<CategoryJpaEntity> categoriesJpaEntities = Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList();

        repository.saveAllAndFlush(categoriesJpaEntities);
    }


}