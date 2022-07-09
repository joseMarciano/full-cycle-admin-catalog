package com.fullcyle.admin.catalog.infastructure.category.presenters;

import com.fullcyle.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.fullcyle.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.fullcyle.admin.catalog.infastructure.category.models.CategoryResponse;
import com.fullcyle.admin.catalog.infastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }

//    Function<CategoryOutput, CategoryApiOutput> present = output -> new CategoryApiOutput(
//            output.id().getValue(),
//            output.name(),
//            output.description(),
//            output.isActive(),
//            output.createdAt(),
//            output.updatedAt(),
//            output.deletedAt()
//    );

}
