package com.fullcyle.admin.catalog.application.video.retrieve.list;

import com.fullcyle.admin.catalog.application.UseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;

public abstract class ListVideoUseCase extends UseCase<SearchQuery, Pagination<VideoListOutput>> {
}
