package com.fullcyle.admin.catalog.e2e.castmember;

import com.fullcyle.admin.catalog.ApiTest;
import com.fullcyle.admin.catalog.E2ETest;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.e2e.MockDsl;
import com.fullcyle.admin.catalog.infastructure.castmember.models.UpdateCastMemberRequest;
import com.fullcyle.admin.catalog.infastructure.castmember.persistence.CastMemberRepository;
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

import static com.fullcyle.admin.catalog.domain.Fixture.CastMembers.type;
import static com.fullcyle.admin.catalog.domain.Fixture.name;
import static com.fullcyle.admin.catalog.domain.castmember.CastMemberType.ACTOR;
import static com.fullcyle.admin.catalog.domain.castmember.CastMemberType.DIRECTOR;
import static org.junit.jupiter.api.Assertions.*;


@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:latest") //put the prod version
            .withPassword("123456").withUsername("root").withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) { // to customize spring properties in runtime
        final var mapperPort = MY_SQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s\n", mapperPort);
        registry.add("mysql.port", () -> mapperPort);
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = name();
        final var expectedType = type();

        final var actualMemberId = givenACastMember(expectedName, expectedType);
        final var actualMember = castMemberRepository.findById(actualMemberId.getValue()).get();

        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingANewCastMemberWithInvalidValues() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = type();
        final var expectedErrorMessage = "'name' should not be null";

        givenACastMemberResult(expectedName, expectedType)
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateThroughAllCastMembers() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember("John", type());
        givenACastMember("Vin Diesel", type());
        givenACastMember("Johnny", type());

        listCastMembers(0, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("John")));


        listCastMembers(1, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Johnny")));
        listCastMembers(2, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")));

        listCastMembers(3, 1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllCastMembers() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember("John", type());
        givenACastMember("Vin Diesel", type());
        givenACastMember("Johnny", type());

        listCastMembers(0, 1, "nny")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Johnny")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllCastMembersByNameDesc() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        givenACastMember("John", type());
        givenACastMember("Vin Diesel", type());
        givenACastMember("Johnny", type());

        listCastMembers(0, 3, "", "name", "desc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Johnny")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("John")))
        ;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetACastMemberByItsIdentifier() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        final var expectedName = name();
        final var expectedType = type();

        final var actualId = givenACastMember(expectedName, expectedType);

        final var actualCastMember = retrieveACastMember(actualId);

        assertEquals(expectedName, actualCastMember.name());
        assertEquals(expectedType, actualCastMember.type());
        assertNotNull(actualCastMember.updatedAt());
        assertNotNull(actualCastMember.createdAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCastMember() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());

        final var aRequest =
                MockMvcRequestBuilders.get("/cast_members/{id}", "123")
                        .with(ApiTest.CAST_MEMBERS_JWT)
                        .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("CastMember with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACastMemberByItsIdentifier() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        assertEquals(0, castMemberRepository.count());


        final var actualId = givenACastMember("Vin", DIRECTOR);

        final var expectedName = "Vin Diesel";
        final var expectedType = ACTOR;

        final var aRequestBody = new UpdateCastMemberRequest(expectedName, expectedType);

        updateACastMember(actualId, aRequestBody)
                .andExpect(MockMvcResultMatchers.status().isOk());

        final var actualCastMember = castMemberRepository.findById(actualId.getValue()).get();


        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertNotNull(actualCastMember.getUpdatedAt());
        assertNotNull(actualCastMember.getCreatedAt());
        assertTrue(actualCastMember.getCreatedAt().isBefore(actualCastMember.getUpdatedAt()));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteCastMemberByIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var actualId = givenACastMember(name(), type());

        deleteACastMember(actualId)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertFalse(this.castMemberRepository.existsById(actualId.getValue()));

    }

    @Test
    public void asACatalogAdminIShouldNotSeeAErrorByDeletingANotExistentCastMember() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        deleteACastMember(CastMemberID.from("123"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertEquals(0, castMemberRepository.count());

    }


}
