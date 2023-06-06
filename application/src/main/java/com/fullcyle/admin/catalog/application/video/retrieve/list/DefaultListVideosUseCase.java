package com.fullcyle.admin.catalog.application.video.retrieve.list;

import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;

public class DefaultListVideosUseCase extends ListVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultListVideosUseCase(final VideoGateway videoGateway) {
        this.videoGateway = videoGateway;
    }

    @Override
    public Pagination<VideoListOutput> execute(final VideoSearchQuery anIn) {
        return this.videoGateway.findAll(anIn).map(VideoListOutput::from);
    }
}
