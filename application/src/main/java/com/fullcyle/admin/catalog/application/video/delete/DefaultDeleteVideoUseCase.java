package com.fullcyle.admin.catalog.application.video.delete;

import com.fullcyle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;
import com.fullcyle.admin.catalog.domain.video.VideoID;

import static java.util.Objects.requireNonNull;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = requireNonNull(videoGateway);
        this.mediaResourceGateway = requireNonNull(mediaResourceGateway);
    }


    @Override
    public void execute(final String anIn) {
        final var aVideoId = VideoID.from(anIn);
        this.videoGateway.deleteById(aVideoId);
        this.mediaResourceGateway.clearResources(aVideoId);
    }
}
