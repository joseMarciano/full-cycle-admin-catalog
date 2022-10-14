package com.fullcyle.admin.catalog.application.genre.create;

import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(genreGateway, times(1)).create(Mockito.argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(genreGateway, times(1)).create(Mockito.argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.nonNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(genreGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);

        verify(genreGateway, times(1)).create(Mockito.argThat(aGenre ->
                Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        //given
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        //when
        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        //given
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        //when
        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        //given
        final var filmes = CategoryID.from("132");
        final var series = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final String expectedName = "Ação";
        final var expectedIsActive = true;

        final var expectedCategories = List.of(filmes, series, documentaries);

        final var expectedErrorMessage = "Some categories could not be found: 132, 789";
        final var expectedErrorCount = 1;


        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(series));

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));


        //when
        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        //given
        final var filmes = CategoryID.from("132");
        final var series = CategoryID.from("456");
        final var documentaries = CategoryID.from("789");

        final String expectedName = " ";
        final var expectedIsActive = true;

        final var expectedCategories = List.of(filmes, series, documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 132, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;


        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(series));

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));


        //when
        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }
}
