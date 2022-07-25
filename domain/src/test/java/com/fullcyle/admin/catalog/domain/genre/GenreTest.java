package com.fullcyle.admin.catalog.domain.genre;


import com.fullcyle.admin.catalog.domain.exceptions.DomainException;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;


        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException =
                assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));


        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final var expectedName = "  ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";


        final var actualException =
                assertThrows(NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));


        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLenghtGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError() {
        final var expectedName = """
                  Assim mesmo, a consolidação das estruturas oferece uma interessante oportunidade para verificação 
                  das diversas correntes de pensamento. O incentivo ao avanço tecnológico, assim como o comprometimento
                  entre as equipes pode nos levar a considerar a reestruturação das direções preferenciais no sentido
                  do progresso. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que o
                  julgamento imparcial das eventualidades agrega valor ao estabelecimento dos relacionamentos 
                  verticais entre as hierarquias. A prática cotidiana prova que a competitividade nas transações
                  comerciais obstaculiza a apreciação da importância dos conhecimentos estratégicos para atingir 
                  a excelência.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException =
                assertThrows(DomainException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnActiveGenre_whenCallDeactivate_shouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;


        final var actualGenre = Genre.newGenre(expectedName, true);
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());


        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();
        actualGenre.deactivate();

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;


        final var actualGenre = Genre.newGenre(expectedName, false);
        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.activate();

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }
}
