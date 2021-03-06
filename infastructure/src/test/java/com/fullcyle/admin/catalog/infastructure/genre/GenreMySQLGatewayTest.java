package com.fullcyle.admin.catalog.infastructure.genre;

import com.fullcyle.admin.catalog.MySQLGatewayTest;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcyle.admin.catalog.infastructure.category.CategoryMySQLGateway;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreJpaEntity;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private GenreMySQLGateway genreMySQLGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
        final var filmes =
                categoryMySQLGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var aGenre =
                Genre.newGenre(expectedName, expectedIsActive)
                        .addCategories(expectedCategories);


        final var expectedId = aGenre.getId();
        assertEquals(0, genreRepository.count());

        final var actualGenre = genreMySQLGateway.create(aGenre);

        assertEquals(1, genreRepository.count());
        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre =
                Genre.newGenre(expectedName, expectedIsActive)
                        .addCategories(expectedCategories);


        final var expectedId = aGenre.getId();
        assertEquals(0, genreRepository.count());

        final var actualGenre = genreMySQLGateway.create(aGenre);

        assertEquals(1, genreRepository.count());
        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        assertNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
        final var filmes =
                categoryMySQLGateway.create(Category.newCategory("Filmes", null, true));

        final var series =
                categoryMySQLGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var aGenre =
                Genre.newGenre("ac", expectedIsActive);


        final var expectedId = aGenre.getId();
        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertEquals("ac", aGenre.getName());
        assertEquals(0, aGenre.getCategories().size());

        final var actualGenre = genreMySQLGateway.update(Genre.with(aGenre)
                .update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, genreRepository.count());
        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.containsAll(actualGenre.getCategories()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertTrue(expectedCategories.containsAll(persistedGenre.getCategoryIDs()));
        assertEquals(actualGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaning_shouldPersistGenre() {
        final var filmes =
                categoryMySQLGateway.create(Category.newCategory("Filmes", null, true));

        final var series =
                categoryMySQLGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre =
                Genre.newGenre("ac", expectedIsActive)
                        .addCategories(List.of(filmes.getId(), series.getId()));

        final var expectedId = aGenre.getId();
        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertEquals("ac", aGenre.getName());
        assertEquals(2, aGenre.getCategories().size());

        final var actualGenre = genreMySQLGateway.update(Genre.with(aGenre)
                .update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, genreRepository.count());
        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(actualGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(actualGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAValidInactiveGenre_whenCallsUpdateGenreActivating_shouldPersistGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre =
                Genre.newGenre(expectedName, false);

        final var expectedId = aGenre.getId();
        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertFalse(aGenre.isActive());
        assertNotNull(aGenre.getDeletedAt());

        final var actualGenre = genreMySQLGateway.update(Genre.with(aGenre)
                .update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, genreRepository.count());
        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(actualGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(actualGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAValidActiveeGenre_whenCallsUpdateGenreInactivating_shouldPersistGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre =
                Genre.newGenre(expectedName, true);

        final var expectedId = aGenre.getId();
        assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        final var actualGenre = genreMySQLGateway.update(Genre.with(aGenre)
                .update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(1, genreRepository.count());
        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, persistedGenre.getName());
        assertEquals(expectedIsActive, persistedGenre.isActive());
        assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        assertEquals(actualGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        assertEquals(actualGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        assertNotNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
        final var aGenre = Genre.newGenre("Ação", true);
        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        assertEquals(1, genreRepository.count());
        genreMySQLGateway.deleteById(aGenre.getId());
        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK() {
        assertEquals(0, genreRepository.count());
        genreMySQLGateway.deleteById(GenreID.from("123"));
        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
        // given
        final var filmes =
                categoryMySQLGateway.create(Category.newCategory("Filmes", null, true));

        final var series =
                categoryMySQLGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        assertEquals(1, genreRepository.count());

        //when
        final var actualGenre = genreMySQLGateway.findById(expectedId).get();

        //then
        assertEquals(1, genreRepository.count());
        assertEquals(expectedId, actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(expectedCategories.containsAll(actualGenre.getCategories()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsFindById_shouldReturnEmpty() {
        final var expectedId = GenreID.from("123");
        assertEquals(0, genreRepository.count());

        //when
        final var actualGenre = genreMySQLGateway.findById(expectedId);

        //then
        assertTrue(actualGenre.isEmpty());
    }

    @Test
    public void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = genreMySQLGateway.findAll(aQuery);

        //then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals(expectedTotal, actualPage.total());

    }

    @ParameterizedTest
    @CsvSource({
            "aç,0,10,1,1,Ação",
            "dr,0,10,1,1,Drama",
            "com,0,10,1,1,Comédia romântica",
            "cien,0,10,1,1,Ficção científica",
            "terr,0,10,1,1,Terror",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final long expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {

        // given
        mockGenres();
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = genreMySQLGateway.findAll(aQuery);

        //then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Ação",
            "name,desc,0,10,5,5,Terror",
            "createdAt,asc,0,10,5,5,Comédia romântica",
            "createdAt,desc,0,10,5,5,Ficção científica"
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final long expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {

        // given
        mockGenres();
        final var expectedTerms = "";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = genreMySQLGateway.findAll(aQuery);

        //then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Ação;Comédia romântica",
            "1,2,2,5,Drama;Ficção científica",
            "2,2,1,5,Terror",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final long expectedItemsCount,
            final long expectedTotal,
            final String expectedGenres
    ) {

        // given
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = genreMySQLGateway.findAll(aQuery);

        //then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        final var genreNames = List.of(expectedGenres.split(";"));
        assertTrue(actualPage.items().stream().map(Genre::getName).toList().containsAll(genreNames));

    }

    private void mockGenres() {
        genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Comédia romântica", true)),
                GenreJpaEntity.from(Genre.newGenre("Ação", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Ficção científica", true))
        ));
    }
}
