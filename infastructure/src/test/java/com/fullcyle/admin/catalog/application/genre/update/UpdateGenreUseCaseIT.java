package com.fullcyle.admin.catalog.application.genre.update;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.DomainException;
import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private DefaultUpdateGenreUseCase useCase;
    @SpyBean
    private CategoryGateway categoryGateway;
    @SpyBean
    private GenreGateway genreGateway;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var acommand =
                UpdateGenreCommand.with(expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(acommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());


    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var acommand =
                UpdateGenreCommand.with(expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));

        assertNull(aGenre.getDeletedAt());
        assertTrue(aGenre.isActive());

        // when
        final var actualOutput = useCase.execute(acommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));

        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                filmes.getId(),
                series.getId()
        );

        final var acommand =
                UpdateGenreCommand.with(expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));


        // when
        final var actualOutput = useCase.execute(acommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());


    }

    @Test
    public void givenAInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        // given
        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var acommand =
                UpdateGenreCommand.with(expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));

        // when
        final var actualException =
                assertThrows(DomainException.class, () -> useCase.execute(acommand));

        // then

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(genreGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(0)).existsByIds(expectedCategories);
        verify(genreGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));
        final var documentaries = CategoryID.from("789");

        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var ids = List.of(
                filmes.getId(),
                series.getId(),
                documentaries
        );

        final var expectedErrorMessageOne = "Some categories could not be found: 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var acommand =
                UpdateGenreCommand.with(expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(ids));

        // when
        final var actualException =
                assertThrows(DomainException.class, () -> useCase.execute(acommand));

        // then
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        verify(genreGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).existsByIds(ids);
        verify(genreGateway, times(0)).update(any());
    }

    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
