package com.fullcyle.admin.catalog.domain.category;

import com.fullcyle.admin.catalog.domain.exceptions.DomainException;
import com.fullcyle.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstaciateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "The category most watched";
        final var expectedIsActive = true;


        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedDescription = "The category most watched";
        final var expectedIsActive = true;


        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        var actualException = Assertions.assertThrows(DomainException.class, () ->
                actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(1, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = " ";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedDescription = "The category most watched";
        final var expectedIsActive = true;


        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        var actualException = Assertions.assertThrows(DomainException.class, () ->
                actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(1, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = "Fi ";
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "The category most watched";
        final var expectedIsActive = true;


        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        var actualException = Assertions.assertThrows(DomainException.class, () ->
                actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(1, actualException.getErrors().size());

    }

    @Test
    public void givenAnInvalidNameLengthGreaterThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = """
                A nível organizacional, o início da atividade geral de formação de atitudes acarreta um processo de ref
                ormulação e modernização das condições inegavelmente apropriadas. Não obstante, o desafiador cenário glo
                balizado agrega valor ao estabelecimento do processo de comunicação como um todo. Percebemos, cada vez
                mais, que a consolidação das estruturas representa uma abertura para a melhoria das diretrizes de
                desenvolvimento para o futuro.
                """;

        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "The category most watched";
        final var expectedIsActive = true;


        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        var actualException = Assertions.assertThrows(DomainException.class, () ->
                actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(1, actualException.getErrors().size());

    }

    @Test
    public void givenAnValidDescriptionEmpty_whenCallNewCategoryAndValidate_thenShouldOk() {
        final var expectedName = "Filmes";
        final var expectedDescription = "   ";
        final var expectedIsActive = true;


        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenAValidIsActive_whenCallNewCategory_thenShouldOk() {
        final var expectedName = "Filmes";
        final var expectedDescription = "The category most watched";
        final var expectedIsActive = false;


        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "The category most watched";
        final var expectedIsActive = true;


        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );


        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertNull(aCategory.getDeletedAt());
        Assertions.assertTrue(aCategory.isActive());

        final var actualCategory = aCategory.deactivate();
        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(actualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertNotEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }
}
