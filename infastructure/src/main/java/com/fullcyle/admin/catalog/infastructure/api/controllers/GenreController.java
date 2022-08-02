package com.fullcyle.admin.catalog.infastructure.api.controllers;

import com.fullcyle.admin.catalog.application.genre.create.CreateGenreCommand;
import com.fullcyle.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.fullcyle.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.fullcyle.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcyle.admin.catalog.application.genre.retrieve.list.GenreListOutput;
import com.fullcyle.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcyle.admin.catalog.application.genre.update.UpdateGenreCommand;
import com.fullcyle.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcyle.admin.catalog.infastructure.api.GenreAPI;
import com.fullcyle.admin.catalog.infastructure.genre.models.CreateGenreRequest;
import com.fullcyle.admin.catalog.infastructure.genre.models.GenreListResponse;
import com.fullcyle.admin.catalog.infastructure.genre.models.GenreResponse;
import com.fullcyle.admin.catalog.infastructure.genre.models.UpdateGenreRequest;
import com.fullcyle.admin.catalog.infastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.util.Objects.requireNonNull;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;

    private final UpdateGenreUseCase updateGenreUseCase;

    private final DeleteGenreUseCase deleteGenreUseCase;

    private final ListGenreUseCase listGenreUseCase;

    public GenreController(final CreateGenreUseCase createGenreUseCase,
                           final GetGenreByIdUseCase getGenreByIdUseCase,
                           final UpdateGenreUseCase updateGenreUseCase,
                           final DeleteGenreUseCase deleteGenreUseCase,
                           final ListGenreUseCase listGenreUseCase) {
        this.createGenreUseCase = requireNonNull(createGenreUseCase);
        this.getGenreByIdUseCase = requireNonNull(getGenreByIdUseCase);
        this.updateGenreUseCase = requireNonNull(updateGenreUseCase);
        this.deleteGenreUseCase = requireNonNull(deleteGenreUseCase);
        this.listGenreUseCase = requireNonNull(listGenreUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id()))
                .body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(final String search,
                                              final int page,
                                              final int perPage,
                                              final String sort,
                                              final String direction) {
        final var aQuery = new SearchQuery(page, perPage, search, sort, direction);

        Pagination<GenreListOutput> result = listGenreUseCase.execute(aQuery);
        return result.map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest body) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                body.name(),
                body.isActive(),
                body.categories()
        );

        final var ouput = updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(ouput);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}
