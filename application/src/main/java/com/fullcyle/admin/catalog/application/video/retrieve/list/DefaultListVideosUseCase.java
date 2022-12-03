package com.fullcyle.admin.catalog.application.video.retrieve.list;

import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;

public class DefaultListVideosUseCase extends ListVideoUseCase {

    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;

    public DefaultListVideosUseCase(final VideoGateway videoGateway,
                                    final CategoryGateway categoryGateway) {
        this.videoGateway = videoGateway;
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<VideoListOutput> execute(final VideoSearchQuery anIn) {
        return this.videoGateway.findAll(anIn).map(VideoListOutput::from);
    }
}
