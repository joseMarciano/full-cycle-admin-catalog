package com.fullcyle.admin.catalog.application.video.retrieve.list;

import com.fullcyle.admin.catalog.application.Fixture;
import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListVideosUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidQuery_whenCallsListVideos_shouldReturnGenres() {
        //given
        final var videos = List.of(
                Fixture.Videos.video(),
                Fixture.Videos.video()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = videos.stream()
                .map((it) -> VideoListOutput.from(it, List.of(Fixture.Categories.aulas())))
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                videos
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);
        when(categoryGateway.findAllById(any()))
                .thenReturn(List.of(Fixture.Categories.aulas()));

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        //when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(videoGateway, times(1))
                .findAll(aQuery);
    }

    @Test
    public void givenAValidQuery_whenCallsListVideosAndResultIsEmpty_shouldReturnGenres() {
        //given
        final var genres = List.<Video>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<VideoListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        //when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(videoGateway, times(1))
                .findAll(aQuery);
    }

    @Test
    public void givenAValidQuery_whenCallsListVideosAndGatewayThrowsRandomError_shouldReturnException() {
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(videoGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        //when
        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () ->
                        useCase.execute(aQuery));

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(videoGateway, times(1))
                .findAll(aQuery);
    }


    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }
}
