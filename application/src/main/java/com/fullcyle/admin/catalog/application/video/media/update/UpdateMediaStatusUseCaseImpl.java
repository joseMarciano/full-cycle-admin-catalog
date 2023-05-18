package com.fullcyle.admin.catalog.application.video.media.update;

import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.video.*;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class UpdateMediaStatusUseCaseImpl extends UpdateMediaStatusUseCase {

    private final VideoGateway videoGateway;

    public UpdateMediaStatusUseCaseImpl(final VideoGateway videoGateway) {
        this.videoGateway = requireNonNull(videoGateway);
    }

    @Override
    public void execute(final UpdateMediaStatusCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aResourceId = aCommand.resourceId();
        final var folder = aCommand.folder();
        final var filename = aCommand.filename();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> notFound(anId));

        final var encodedPath = "%s/%s".formatted(folder, filename);

        if (matches(aResourceId, aVideo.getVideo().orElse(null))) {
            updateVideo(VideoMediaType.VIDEO, aCommand.status(), aVideo, encodedPath);
            return;
        }


        if (matches(aResourceId, aVideo.getTrailer().orElse(null))) {
            updateVideo(VideoMediaType.TRAILER, aCommand.status(), aVideo, encodedPath);
            return;
        }


    }

    private void updateVideo(final VideoMediaType aType, final MediaStatus aStatus, final Video aVideo, final String encodedPath) {
        switch (aStatus) {
            case COMPLETED -> aVideo.completed(aType, encodedPath);
            case PROCESSING -> aVideo.processing(aType);
        }

        this.videoGateway.update(aVideo);
    }


    private boolean matches(final String anId, final AudioVideoMedia audioVideoMedia) {
        return ofNullable(audioVideoMedia)
                .map(it -> it.id().equals(anId))
                .orElse(false);
    }


    private NotFoundException notFound(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
