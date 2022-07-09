package com.fullcyle.admin.catalog.application.category.retrieve.list;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategorySearchQuery;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryJpaEntity;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class ListCategoryUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("NetFlix", "TItulos da netflix", true),
                Category.newCategory("Amazon Originals", "Categoria da amazon", true),
                Category.newCategory("Documentarios", null, true),
                Category.newCategory("Sports", null, true),
                Category.newCategory("Kids", "Categoria para crianças", true),
                Category.newCategory("Series", null, true)
        ).map(CategoryJpaEntity::from).collect(Collectors.toList());

        repository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "sdai2309r8f";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedItemsTotal = 0;

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedItemsTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,NetFlix",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "crianças,0,10,1,1,Kids",
            "da amazon,0,10,1,1,Amazon Originals"
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Filmes",
            "createdAt,desc,0,10,7,7,Series",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {

        final var expectedTerms = "";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentarios",
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,NetFlix;Series",
            "3,2,1,7,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ) {

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (String expectedName : expectedCategoriesName.split(";")) {
            final var actualName = actualResult.items().get(index).name();
            assertEquals(expectedName, actualName);
            index++;
        }
    }
}
