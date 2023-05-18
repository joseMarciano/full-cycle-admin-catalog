package com.fullcyle.admin.catalog.application.video.media.get;

import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.validation.Error;
import com.fullcyle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcyle.admin.catalog.domain.video.VideoID;
import com.fullcyle.admin.catalog.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOuput execute(final GetMediaCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aType = VideoMediaType.of(aCommand.mediaType())
                .orElseThrow(() -> typeNotFound(aCommand.mediaType()));

        final var aResource =
                this.mediaResourceGateway.getResource(anId, aType)
                        .orElseThrow(() -> notFound(aCommand.videoId(), aCommand.mediaType()));

        return MediaOuput.with(aResource);
    }

    private NotFoundException typeNotFound(final String mediaType) {
        return NotFoundException.with(new Error("Media type  %s does not exists".formatted(mediaType)));
    }

    private NotFoundException notFound(final String anId, final String mediaType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(mediaType, anId)));
    }
}
