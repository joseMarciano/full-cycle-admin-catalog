package com.fullcyle.admin.catalog.application.genre.retrieve.list;

import com.fullcyle.admin.catalog.application.UseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase  extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
