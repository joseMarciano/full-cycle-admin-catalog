package com.fullcyle.admin.catalog.application.category.update;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.DomainException;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryJpaEntity;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private DefaultUpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory(
                "Film",
                null,
                true
        );

        Assertions.assertEquals(0, repository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var expectedId = aCategory.getId();

        save(aCategory);
        Assertions.assertEquals(1, repository.count());


        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedActive
        );
        ;

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());
        assertEquals(expectedId.getValue(), actualOutput.id().getValue());

        final var actualCategory =
                repository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(actualCategory.getCreatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() {
        Assertions.assertEquals(0, repository.count());

        final var aCategory = Category.newCategory(
                "Film",
                null,
                true
        );

        save(aCategory);

        Assertions.assertEquals(1, repository.count());

        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();


        final var aCommand =
                UpdateCategoryCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedDescription,
                        expectedActive);

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(0)).update(any());

    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        Assertions.assertEquals(0, repository.count());

        final var aCategory = Category.newCategory(
                "Film",
                null,
                true
        );


        save(aCategory);

        Assertions.assertEquals(1, repository.count());

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = false;
        final var expectedId = aCategory.getId();


        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedActive
        );

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory =
                repository.findById(actualOutput.id().getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(actualCategory.getCreatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());


    }

    @Test
    public void givenACommandWithInvalidID__whenCallsUpdateCategory_shouldReturnNotFoundException() {
        Assertions.assertEquals(0, repository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var expectedId = "any_id";
        final var expectedErrorMessage = "Category with ID any_id was not found";
        final var expectedErrorCount = 1;


        final var aCategory = Category.newCategory(
                "Film",
                null,
                true
        );

        save(aCategory);

        Assertions.assertEquals(1, repository.count());

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedActive
        );

        Mockito.doReturn(Optional.empty())
                .when(categoryGateway)
                .findById(eq(CategoryID.from(expectedId)));

        DomainException actualException =
                assertThrows(DomainException.class, () -> useCase.execute(aCommand).getLeft());


        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        final var actualCategory =
                repository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());

    }

    private void save(final Category... categories) {
        List<CategoryJpaEntity> categoriesJpaEntities = Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList();

        repository.saveAllAndFlush(categoriesJpaEntities);
    }
}
