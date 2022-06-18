package com.fullcyle.admin.catalog.application.category.delete;

import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {


    private final CategoryGateway gateway;

    public DefaultDeleteCategoryUseCase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(String anIn) {
        this.gateway.deleteById(CategoryID.from(anIn));
    }
}
