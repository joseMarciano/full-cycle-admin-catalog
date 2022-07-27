package com.fullcyle.admin.catalog.application.genre.update;

import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var acommand =
                UpdateGenreCommand.with(expectedId.getValue(),
                        expectedName,
                        expectedIsActive,
                        asString(expectedCategories));

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGenre)));

        when(genreGateway.update(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(acommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(genreGateway, times(1)).findById(expectedId);

        verify(genreGateway, times(1)).update(argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && Objects.isNull(aUpdatedGenre.getDeletedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
        ));

    }

    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
