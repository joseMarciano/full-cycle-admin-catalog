package com.fullcyle.admin.catalog.application.category.retrieve.list;

import com.fullcyle.admin.catalog.application.UseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
