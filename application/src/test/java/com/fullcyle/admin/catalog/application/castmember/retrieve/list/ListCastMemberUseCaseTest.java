package com.fullcyle.admin.catalog.application.castmember.retrieve.list;

import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.Fixture;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ListCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMember_shouldReturnCastMembers() {
        //given
        final var castMembers = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = castMembers.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                castMembers
        );

        when(castMemberGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
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

        verify(castMemberGateway, times(1))
                .findAll(aQuery);
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMemberAndResultIsEmpty_shouldReturnCastMembers() {
        //given
        final var castMembers = List.<CastMember>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<CastMemberListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                castMembers
        );

        when(castMemberGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery = new SearchQuery(
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

        verify(castMemberGateway, times(1))
                .findAll(aQuery);
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMemberAndGatewayThrowsRandomError_shouldReturnException() {
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(castMemberGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new SearchQuery(
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
        verify(castMemberGateway, times(1))
                .findAll(aQuery);
    }
}
