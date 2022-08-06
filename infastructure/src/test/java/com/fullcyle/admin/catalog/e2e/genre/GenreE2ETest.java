package com.fullcyle.admin.catalog.e2e.genre;


import com.fullcyle.admin.catalog.E2ETest;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.e2e.MockDsl;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;


    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");


    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MY_SQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName, expectedActive, expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getCreatedAt());
        assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);

        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes);

        final var actualId = givenAGenre(expectedName, expectedActive, expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertTrue(expectedCategories.size() == actualGenre.getCategories().size()
                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getCreatedAt());
        assertNull(actualGenre.getDeletedAt());

    }


    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateThroughAllGenres() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação", true, List.of());
        givenAGenre("Esportes", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Ação")));

        listGenres(1, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")));

        listGenres(2, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Esportes")));

        listGenres(3, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação", true, List.of());
        givenAGenre("Esportes", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 1, "dr")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByNameDesc() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação", true, List.of());
        givenAGenre("Esportes", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 3, "", "name", "desc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Esportes")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Drama")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Ação")))
        ;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Drama";
        final var expectedActive = true;

        final var expectedCategories = List.of(givenACategory("Filmes", null, true));

        final var actualId = givenAGenre(expectedName, expectedActive, expectedCategories);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                        && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(expectedActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNull(actualGenre.deletedAt());

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var aRequest =
                MockMvcRequestBuilders.get("/genres/{id}", "123")
                        .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("Genre with ID 123 was not found")));
    }

}
