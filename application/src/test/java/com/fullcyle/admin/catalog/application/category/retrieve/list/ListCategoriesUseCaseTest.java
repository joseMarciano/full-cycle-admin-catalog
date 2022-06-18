package com.fullcyle.admin.catalog.application.category.retrieve.list;


import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategorySearchQuery;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories(){

        List<Category> categories = List.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Series", null, true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Pagination<Category> expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                categories.size(),
                categories
        );


        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories(){

        List<Category> categories = Collections.emptyList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Pagination<Category> expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                categories.size(),
                categories
        );


        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException(){
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);



        when(categoryGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
