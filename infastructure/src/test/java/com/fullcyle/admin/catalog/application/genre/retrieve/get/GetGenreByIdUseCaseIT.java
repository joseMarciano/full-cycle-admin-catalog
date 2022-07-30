package com.fullcyle.admin.catalog.application.genre.retrieve.get;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private GetGenreByIdUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        //given
        final var filmes =
                categoryGateway.create(Category.newCategory("Filmes", null, true));


        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                filmes.getId()
        );

        final var aGenre =
                Genre.newGenre(expectedName, expectedIsActive)
                        .addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        genreGateway.create(aGenre);

        //when
        final var actualGenre = useCase.execute(expectedId.getValue());

        //then
        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(asString(expectedCategories), actualGenre.categories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
    }


    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        //given
        final var expctedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () ->
                useCase.execute(expectedId.getValue())
        );

        //then
        assertEquals(expctedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
