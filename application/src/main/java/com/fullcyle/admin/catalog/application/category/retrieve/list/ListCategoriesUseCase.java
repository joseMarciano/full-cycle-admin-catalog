package com.fullcyle.admin.catalog.application.category.retrieve.list;

import com.fullcyle.admin.catalog.application.UseCase;
import com.fullcyle.admin.catalog.domain.category.CategorySearchQuery;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
