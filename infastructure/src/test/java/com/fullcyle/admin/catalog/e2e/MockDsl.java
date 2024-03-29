package com.fullcyle.admin.catalog.e2e;

import com.fullcyle.admin.catalog.ApiTest;
import com.fullcyle.admin.catalog.domain.Identifier;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.infastructure.castmember.models.CastMemberResponse;
import com.fullcyle.admin.catalog.infastructure.castmember.models.CreateCastMemberRequest;
import com.fullcyle.admin.catalog.infastructure.castmember.models.UpdateCastMemberRequest;
import com.fullcyle.admin.catalog.infastructure.category.models.CategoryResponse;
import com.fullcyle.admin.catalog.infastructure.category.models.CreateCategoryRequest;
import com.fullcyle.admin.catalog.infastructure.category.models.UpdateCategoryRequest;
import com.fullcyle.admin.catalog.infastructure.configuration.json.Json;
import com.fullcyle.admin.catalog.infastructure.genre.models.CreateGenreRequest;
import com.fullcyle.admin.catalog.infastructure.genre.models.GenreResponse;
import com.fullcyle.admin.catalog.infastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.function.Function;

public interface MockDsl {

    MockMvc mvc();

    /**
     * Cast Member
     */

    default CastMemberID givenACastMember(String aName, CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        final var actualId = this.given("/cast_members", aRequestBody);
        return CastMemberID.from(actualId);
    }

    default ResultActions givenACastMemberResult(String aName, CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberRequest(aName, aType);
        return this.givenResult("/cast_members", aRequestBody);
    }

    default ResultActions listCastMembers(final int page, final int perPage, String film) throws Exception {
        return listCastMembers(page, perPage, film, "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return listCastMembers(page, perPage, "", "", "");
    }

    default ResultActions listCastMembers(final int page,
                                          final int perPage,
                                          final String search,
                                          final String sort,
                                          final String direction) throws Exception {
        return this.list("/cast_members", page, perPage, search, sort, direction);
    }

    default CastMemberResponse retrieveACastMember(final Identifier anId) throws Exception {
        return this.retrieve("/cast_members", anId, CastMemberResponse.class);
    }

    default ResultActions updateACastMember(final Identifier anId, final UpdateCastMemberRequest aRequest) throws Exception {
        return this.update("/cast_members", anId, aRequest);
    }

    default ResultActions deleteACastMember(final Identifier anId) throws Exception {
        return this.delete("/cast_members", anId);
    }

    /**
     * Category
     */

    default ResultActions deleteACategory(final Identifier anId) throws Exception {
        return this.delete("/categories", anId);
    }

    default CategoryID givenACategory(String aName, String aDescription, boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = this.given("/categories", aRequestBody);
        return CategoryID.from(actualId);
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

    default ResultActions updateACategory(final Identifier anId, final UpdateCategoryRequest aRequest) throws Exception {
        return this.update("/categories", anId, aRequest);
    }

    /**
     * Genre
     */

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", aRequestBody);

        return GenreID.from(actualId);
    }

    default ResultActions listGenres(final int page, final int perPage, String film) throws Exception {
        return listGenres(page, perPage, film, "", "");
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page,
                                     final int perPage,
                                     final String search,
                                     final String sort,
                                     final String direction) throws Exception {
        return this.list("/genres", page, perPage, search, sort, direction);
    }


    default GenreResponse retrieveAGenre(final Identifier anId) throws Exception {
        return this.retrieve("/genres", anId, GenreResponse.class);
    }

    default ResultActions updateAGenre(final Identifier anId, final UpdateGenreRequest aRequest) throws Exception {
        return this.update("/genres", anId, aRequest);
    }

    default ResultActions deleteAGenre(final Identifier anId) throws Exception {
        return this.delete("/genres", anId);
    }


    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }

    private ResultActions givenResult(final String url, final Object body) throws Exception {
        final var aRequest =
                MockMvcRequestBuilders.post(url)
                        .with(ApiTest.ADMIN_JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(body));

        return this.mvc()
                .perform(aRequest);
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest =
                MockMvcRequestBuilders.post(url)
                        .with(ApiTest.ADMIN_JWT)
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
                        .with(ApiTest.ADMIN_JWT)
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
                        .with(ApiTest.ADMIN_JWT)
                        .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc().perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest =
                MockMvcRequestBuilders.delete("%s/%s".formatted(url, anId.getValue()))
                        .with(ApiTest.ADMIN_JWT)
                        .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object aRequestBody) throws Exception {
        final var aRequest =
                MockMvcRequestBuilders.put("%s/%s".formatted(url, anId.getValue()), anId.getValue())
                        .with(ApiTest.ADMIN_JWT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }

}
