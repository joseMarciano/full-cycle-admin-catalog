package com.fullcyle.admin.catalog.application.category.create;


import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class CreateCategoryUseCaseTest {

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {


        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedActive);

        final var categoryGateway = mock(CategoryGateway.class);
        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())

        ));
    }
}
