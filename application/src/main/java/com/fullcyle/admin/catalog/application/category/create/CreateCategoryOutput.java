package com.fullcyle.admin.catalog.application.category.create;

import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryID;

public record CreateCategoryOutput(CategoryID id) {

    public static CreateCategoryOutput from(Category anCategory) {
        return new CreateCategoryOutput(anCategory.getId());
    }
}
