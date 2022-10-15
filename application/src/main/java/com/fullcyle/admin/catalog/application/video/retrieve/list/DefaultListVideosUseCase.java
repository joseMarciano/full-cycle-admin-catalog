package com.fullcyle.admin.catalog.application.video.retrieve.list;

import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;

import java.util.function.Function;

public class DefaultListVideosUseCase extends ListVideoUseCase {

    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;

    public DefaultListVideosUseCase(final VideoGateway videoGateway,
                                    final CategoryGateway categoryGateway) {
        this.videoGateway = videoGateway;
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<VideoListOutput> execute(final SearchQuery anIn) {
        final Function<Video, VideoListOutput> buildListOutput = video -> {
            final var categories = this.categoryGateway.findAllById(video.getCategories());
            return VideoListOutput.from(video, categories);
        };

        return this.videoGateway.findAll(anIn).map(buildListOutput);
    }
}
