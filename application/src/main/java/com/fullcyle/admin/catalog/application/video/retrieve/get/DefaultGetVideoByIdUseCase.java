package com.fullcyle.admin.catalog.application.video.retrieve.get;

import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;
import com.fullcyle.admin.catalog.domain.video.VideoID;

import java.util.function.Supplier;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = videoGateway;
    }

    @Override
    public GetVideoByIdOutput execute(final String anIn) {
        VideoID anId = VideoID.from(anIn);
        return this.videoGateway.findById(anId)
                .map(GetVideoByIdOutput::from)
                .orElseThrow(notFound(anId));
    }

    private Supplier<NotFoundException> notFound(final VideoID anId) {
        return () -> NotFoundException.with(Video.class, anId);
    }


}
