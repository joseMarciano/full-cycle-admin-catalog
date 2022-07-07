package com.fullcyle.admin.catalog.application.category.create;

import com.fullcyle.admin.catalog.domain.category.Category;

public record CreateCategoryOutput(String id) {

    public static CreateCategoryOutput from(final String anId) {
        return new CreateCategoryOutput(anId);
    }

    public static CreateCategoryOutput from(final Category anCategory) {
        return new CreateCategoryOutput(anCategory.getId().getValue());
    }
}
