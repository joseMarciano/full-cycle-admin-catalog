package com.fullcyle.admin.catalog.infastructure.genre;

import com.fullcyle.admin.catalog.MySQLGatewayTest;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.infastructure.category.CategoryMySQLGateway;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreJpaEntity;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
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
}
