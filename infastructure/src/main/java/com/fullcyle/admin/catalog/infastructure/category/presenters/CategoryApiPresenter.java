package com.fullcyle.admin.catalog.infastructure.category.presenters;

import com.fullcyle.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.fullcyle.admin.catalog.infastructure.category.models.CategoryApiOutput;

import java.util.function.Function;

public interface CategoryApiPresenter {

//    static CategoryApiOutput present(final CategoryOutput output) {
//        return new CategoryApiOutput(
//                output.id().getValue(),
//                output.name(),
//                output.description(),
//                output.isActive(),
//                output.createdAt(),
//                output.updatedAt(),
//                output.deletedAt()
//        );
//    }

    Function<CategoryOutput, CategoryApiOutput> present = output -> new CategoryApiOutput(
            output.id().getValue(),
            output.name(),
            output.description(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt(),
            output.deletedAt()
    );
}
