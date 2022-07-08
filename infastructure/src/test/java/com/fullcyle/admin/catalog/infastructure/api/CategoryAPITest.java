package com.fullcyle.admin.catalog.infastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcyle.admin.catalog.ControllerTest;
import com.fullcyle.admin.catalog.application.category.create.CreateCategoryOutput;
import com.fullcyle.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.fullcyle.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.fullcyle.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.DomainException;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.validation.Error;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;
import com.fullcyle.admin.catalog.infastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;

        final var aCommand = new CreateCategoryApiInput(expectedName, expectedDescription, expectedActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

        //when
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("Location", "/categories/123"),
                        MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123"))
                );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_shouldReturnNotification() throws Exception {
        //given
        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var expectedMessage = "'name' should not  be null";

        final var aCommand = new CreateCategoryApiInput(expectedName, expectedDescription, expectedActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
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
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage))
                );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_shouldReturnDomainException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var expectedMessage = "'name' should not  be null";

        final var aCommand = new CreateCategoryApiInput(expectedName, expectedDescription, expectedActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        //when
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                        MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedMessage)),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage))
                );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        //given
        final var expectedName = "Filmes";
        final var expectedDescription = "Most watched";
        final var expectedActive = true;
        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedActive
        );

        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(anyString()))
                .thenReturn(CategoryOutput.from(aCategory));

        //when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", Matchers.equalTo(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1)).execute(expectedId);
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        //given
        final var expectedErrorMessage = "Category with ID any_id was not found";
        final var expectedId = CategoryID.from("any_id");

        when(getCategoryByIdUseCase.execute(anyString()))
                .thenThrow(NotFoundException.with(
                        Category.class,
                        expectedId
                ));

        //when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

    }
}
