package com.fullcyle.admin.catalog.application.video.retrieve.list;

import com.fullcyle.admin.catalog.application.UseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.VideoSearchQuery;

public abstract class ListVideoUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
