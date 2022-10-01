package com.fullcyle.admin.catalog.infastructure.api.controllers;

import com.fullcyle.admin.catalog.application.castmember.create.CreateCastMemberCommand;
import com.fullcyle.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.fullcyle.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.fullcyle.admin.catalog.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.update.UpdateCastMemberCommand;
import com.fullcyle.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcyle.admin.catalog.infastructure.api.CastMemberAPI;
import com.fullcyle.admin.catalog.infastructure.castmember.models.CastMemberListResponse;
import com.fullcyle.admin.catalog.infastructure.castmember.models.CastMemberResponse;
import com.fullcyle.admin.catalog.infastructure.castmember.models.CreateCastMemberRequest;
import com.fullcyle.admin.catalog.infastructure.castmember.models.UpdateCastMemberRequest;
import com.fullcyle.admin.catalog.infastructure.castmember.presenters.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.util.Objects.requireNonNull;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMemberUseCase listCastMemberUseCase;


    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase,
                                final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
                                final UpdateCastMemberUseCase updateCastMemberUseCase,
                                final DeleteCastMemberUseCase deleteCastMemberUseCase,
                                final ListCastMemberUseCase listCastMemberUseCase) {
        this.createCastMemberUseCase = requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = getCastMemberByIdUseCase;
        this.updateCastMemberUseCase = updateCastMemberUseCase;
        this.deleteCastMemberUseCase = deleteCastMemberUseCase;
        this.listCastMemberUseCase = listCastMemberUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        var aCommand = CreateCastMemberCommand.with(
                input.name(),
                input.type()
        );

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public Pagination<CastMemberListResponse> list(final String search,
                                                   final int page,
                                                   final int perPage,
                                                   final String sort,
                                                   final String direction) {
        final var aQuery = new SearchQuery(page, perPage, search, sort, direction);

        Pagination<CastMemberListOutput> result = listCastMemberUseCase.execute(aQuery);
        return result.map(CastMemberPresenter::present);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest body) {
        final var aCommand = UpdateCastMemberCommand.with(
                id,
                body.name(),
                body.type()
        );

        final var output = updateCastMemberUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}
