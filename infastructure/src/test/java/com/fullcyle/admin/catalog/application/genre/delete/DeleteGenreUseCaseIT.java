package com.fullcyle.admin.catalog.application.genre.delete;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        //given
        final var aGenre = genreGateway.create(Genre.newGenre("Ação", true));
        final var expectedId = aGenre.getId();

        //when
        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));


        // then
        assertEquals(0, genreRepository.count());

    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        //given
        genreGateway.create(Genre.newGenre("Ação", true));
        final var expectedId = GenreID.from("123");

        //when
        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        assertEquals(1, genreRepository.count());
    }
}
