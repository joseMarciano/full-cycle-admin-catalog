package com.fullcyle.admin.catalog.infastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcyle.admin.catalog.ApiTest;
import com.fullcyle.admin.catalog.ControllerTest;
import com.fullcyle.admin.catalog.application.castmember.create.CreateCastMemberOutput;
import com.fullcyle.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.fullcyle.admin.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.fullcyle.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.fullcyle.admin.catalog.application.castmember.retrieve.list.DefaultListCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.update.UpdateCastMemberOutput;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.validation.Error;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;
import com.fullcyle.admin.catalog.infastructure.castmember.models.CreateCastMemberRequest;
import com.fullcyle.admin.catalog.infastructure.castmember.models.UpdateCastMemberRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static com.fullcyle.admin.catalog.domain.Fixture.CastMembers.type;
import static com.fullcyle.admin.catalog.domain.Fixture.name;
import static com.fullcyle.admin.catalog.domain.castmember.CastMemberType.DIRECTOR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @MockBean
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    @MockBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private DefaultListCastMemberUseCase listCastMemberUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnCastMemberId() throws Exception {
        //given
        final var expectedName = name();
        final var expectedType = type();

        final var aCommand = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenReturn(CreateCastMemberOutput.from("123"));

        //when
        final var request = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("Location", "/cast_members/123"),
                        MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123"))
                );

        verify(createCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_shouldReturnNotification() throws Exception {
        //given
        final String expectedName = null;
        final var expectedType = type();

        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        //when
        final var request = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                        MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
                );

        verify(createCastMemberUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCastMemberById_shouldReturnCastMember() throws Exception {
        // given
        final var expectedName = name();
        final var expectedType = type();

        final var aCastMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aCastMember.getId().getValue();

        when(getCastMemberByIdUseCase.execute(expectedId))
                .thenReturn(CastMemberOutput.from(aCastMember));

        // when
        final var aRequest = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        final var aResponse = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // then
        aResponse
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo(expectedType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aCastMember.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aCastMember.getUpdatedAt().toString())));

        verify(getCastMemberByIdUseCase).execute(expectedId);

    }

    @Test
    public void givenAInvalidId_whenCallsGetCastMemberById_shouldNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        when(getCastMemberByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        // when
        final var aRequest = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        final var aResponse = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // then
        aResponse
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        verify(getCastMemberByIdUseCase).execute(expectedId.getValue());

    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnCastMemberId() throws Exception {
        // given
        final var expectedName = name();
        final var expectedType = type();
        final var aCastMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aCastMember.getId().getValue();

        final var aCommand = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any()))
                .thenReturn(UpdateCastMemberOutput.from(aCastMember));

        // when
        final var aRequest = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());


        // then
        aResponse.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        verify(updateCastMemberUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));

    }

    @Test
    public void givenAnInValidCommand_whenCallsUpdateCastMember_shouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedType = type();
        final var expectedErrorMessage = "'name' should not be null";

        final var aCastMember = CastMember.newMember(name(), expectedType);
        final var expectedId = aCastMember.getId().getValue();

        final var aCommand = new UpdateCastMemberRequest(expectedName, expectedType);

        when(updateCastMemberUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        // when
        final var aRequest = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var aResponse = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());


        // then
        aResponse.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)))
        ;

        verify(updateCastMemberUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedType, cmd.type())
        ));

    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldBeOK() throws Exception {
        //given
        final var expectedId = "132";
        //when

        doNothing()
                .when(deleteCastMemberUseCase).execute(expectedId);

        final var aRequest = MockMvcRequestBuilders.delete("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest);
        //then
        aResponse.andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deleteCastMemberUseCase).execute(expectedId);
    }

    @Test
    public void givenValidParams_whenCallsListCastMembers_shouldReturnCastMembers() throws Exception {
        // given
        final var aCastMember = CastMember.newMember("Vin", DIRECTOR);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CastMemberListOutput.from(aCastMember));

        when(listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));
        // when
        final var aRequest = MockMvcRequestBuilders.get("/cast_members/")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        final var aResponse = this.mvc.perform(aRequest);
        // then

        aResponse.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aCastMember.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aCastMember.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(aCastMember.getType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aCastMember.getCreatedAt().toString())));

        verify(listCastMemberUseCase).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }

}
