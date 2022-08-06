package com.fullcyle.admin.catalog.e2e;

import com.fullcyle.admin.catalog.domain.Identifier;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.infastructure.category.models.CategoryResponse;
import com.fullcyle.admin.catalog.infastructure.category.models.CreateCategoryRequest;
import com.fullcyle.admin.catalog.infastructure.configuration.json.Json;
import com.fullcyle.admin.catalog.infastructure.genre.models.CreateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.function.Function;

public interface MockDsl {

    MockMvc mvc();

    default CategoryID givenACategory(String aName, String aDescription, boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);
        return CategoryID.from(actualId);
    }


    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", aRequestBody);

        return GenreID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage, String film) throws Exception {
        return listCategories(page, perPage, film, "", "");
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(final int page,
                                         final int perPage,
                                         final String search,
                                         final String sort,
                                         final String direction) throws Exception {
        return this.list("/categories", page, perPage, search, sort, direction);
    }


    default CategoryResponse retrieveACategory(final Identifier anId) throws Exception {
        return this.retrieve("/categories", anId, CategoryResponse.class);
    }


    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest =
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(body));

        final var actualId = this.mvc()
                .perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location").replace("%s/".formatted(url), "");

        return actualId;
    }

    private ResultActions list(
            final String url,
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction) throws Exception {
        final var aRequest =
                MockMvcRequestBuilders.get(url)
                        .queryParam("page", String.valueOf(page))
                        .queryParam("perPage", String.valueOf(perPage))
                        .queryParam("search", search)
                        .queryParam("sort", sort)
                        .queryParam("dir", direction)
                        .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }


    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest =
                MockMvcRequestBuilders.get("%s/%s".formatted(url, anId.getValue()), anId)
                        .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc().perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Json.readValue(json, clazz);
    }

}
