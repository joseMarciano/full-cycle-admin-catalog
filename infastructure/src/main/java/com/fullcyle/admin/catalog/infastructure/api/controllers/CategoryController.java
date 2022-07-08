package com.fullcyle.admin.catalog.infastructure.api.controllers;

import com.fullcyle.admin.catalog.application.category.create.CreateCategoryCommand;
import com.fullcyle.admin.catalog.application.category.create.CreateCategoryOutput;
import com.fullcyle.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.fullcyle.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;
import com.fullcyle.admin.catalog.infastructure.api.CategoryAPI;
import com.fullcyle.admin.catalog.infastructure.category.models.CategoryApiOutput;
import com.fullcyle.admin.catalog.infastructure.category.models.CreateCategoryApiInput;
import com.fullcyle.admin.catalog.infastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@RestController
public class CategoryController implements CategoryAPI {


    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                ofNullable(input.active()).orElse(true)
        );

        final Function<Notification, ResponseEntity<?>> onError = (output) ->
                ResponseEntity.unprocessableEntity().body(output);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = (output) ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CategoryApiOutput getById(String id) {
        return CategoryApiPresenter.present.apply(this.getCategoryByIdUseCase.execute(id));
    }
}
