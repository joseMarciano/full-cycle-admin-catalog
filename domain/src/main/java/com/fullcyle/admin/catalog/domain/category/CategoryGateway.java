package com.fullcyle.admin.catalog.domain.category;

import com.fullcyle.admin.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category aCategory);

    void deleteById(CategoryID anID);

    Optional<Category> findById(CategoryID anID);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery aQuery);

}
