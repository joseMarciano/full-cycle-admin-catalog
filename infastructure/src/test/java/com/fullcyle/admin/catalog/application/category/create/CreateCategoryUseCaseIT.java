package com.fullcyle.admin.catalog.application.category.create;


import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private DefaultCreateCategoryUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_shouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, categoryRepository.count());


        final var aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);


        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(0, categoryRepository.count());

        verify(categoryGateway, times(0)).create(any());

    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = false;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());


        final var actualCategory =
                categoryRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;


        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);

        //Using when is annotated with @SpyBean
        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).create(any());
//        when(categoryGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        Notification notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());


    }
}
