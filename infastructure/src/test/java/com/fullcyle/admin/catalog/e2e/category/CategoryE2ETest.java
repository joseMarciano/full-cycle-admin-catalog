package com.fullcyle.admin.catalog.e2e.category;

import com.fullcyle.admin.catalog.E2ETest;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.infastructure.category.models.CategoryResponse;
import com.fullcyle.admin.catalog.infastructure.category.models.CreateCategoryRequest;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import com.fullcyle.admin.catalog.infastructure.configuration.json.Json;
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

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:latest") //put the prod version
            .withPassword("123456").withUsername("root").withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) { // to customize spring propeties in runtime
        final var mapperPort = MY_SQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s\n", mapperPort);
        registry.add("mysql.port", () -> mapperPort);
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedActive);

        final var actualCategory = retrieveACategory(actualId.getValue());

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedActive, actualCategory.active());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNull(actualCategory.deletedAt());

    }

    private CategoryResponse retrieveACategory(String anId) throws Exception {
        final var aRequest =
                MockMvcRequestBuilders.get("/categories/{id}", anId)
                        .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Json.readValue(json, CategoryResponse.class);
    }

    private CategoryID givenACategory(String aName, String aDescription, boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

        final var aRequest = MockMvcRequestBuilders.post("/categories").contentType(MediaType.APPLICATION_JSON).content(Json.writeValueAsString(aRequestBody));

        final var actualId = this.mvc.perform(aRequest).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getHeader("Location").replace("/categories/", "");

        return CategoryID.from(actualId);
    }

}
