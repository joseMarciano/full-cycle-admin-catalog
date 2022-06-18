package com.fullcyle.admin.catalog.application.category.delete;

import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = Category.newCategory(
                "filmes",
                "Most watched",
                true
        );

        final var expectedId = aCategory.getId();

        Mockito.doNothing()
                .when(categoryGateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));

    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedId = CategoryID.from("123");

        Mockito.doNothing()
                .when(categoryGateway).deleteById(eq(expectedId));


        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
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

}
