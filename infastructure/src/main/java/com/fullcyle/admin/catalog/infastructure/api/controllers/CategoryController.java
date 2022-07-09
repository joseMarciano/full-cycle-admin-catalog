package com.fullcyle.admin.catalog.infastructure.api.controllers;

import com.fullcyle.admin.catalog.application.category.create.CreateCategoryCommand;
import com.fullcyle.admin.catalog.application.category.create.CreateCategoryOutput;
import com.fullcyle.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.fullcyle.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.fullcyle.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcyle.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.fullcyle.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;
import com.fullcyle.admin.catalog.infastructure.api.CategoryAPI;
import com.fullcyle.admin.catalog.infastructure.category.models.CategoryApiOutput;
import com.fullcyle.admin.catalog.infastructure.category.models.CreateCategoryApiInput;
import com.fullcyle.admin.catalog.infastructure.category.models.UpdateCategoryApiInput;
import com.fullcyle.admin.catalog.infastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@RestController
public class CategoryController implements CategoryAPI {


    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase,
                              UpdateCategoryUseCase updateCategoryUseCase,
                              DeleteCategoryUseCase deleteCategoryUseCase) {
        this.createCategoryUseCase = requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = requireNonNull(deleteCategoryUseCase);
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
    public CategoryApiOutput getById(final String id) {
        return CategoryApiPresenter.present.apply(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiInput input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                ofNullable(input.active()).orElse(true)
        );

        final Function<Notification, ResponseEntity<?>> onError = (output) ->
                ResponseEntity.unprocessableEntity().body(output);

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, ResponseEntity::ok);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}
