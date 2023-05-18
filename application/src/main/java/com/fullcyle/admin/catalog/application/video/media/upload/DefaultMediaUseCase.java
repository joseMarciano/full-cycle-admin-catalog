package com.fullcyle.admin.catalog.application.video.media.upload;

import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;
import com.fullcyle.admin.catalog.domain.video.VideoID;

import static java.util.Objects.requireNonNull;

public class DefaultMediaUseCase extends UploadMediaUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultMediaUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = requireNonNull(videoGateway);
        this.mediaResourceGateway = requireNonNull(mediaResourceGateway);
    }

    @Override
    public UploadMediaOuput execute(final UploadMediaCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aResource = aCommand.videoResource();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> notFound(anId));

        switch (aResource.type()) {
            case THUMBNAIL_HALF -> aVideo.setThumbnailHalf(this.mediaResourceGateway.storeImage(anId, aResource));
            case BANNER -> aVideo.setBanner(this.mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL -> aVideo.setThumbnail(this.mediaResourceGateway.storeImage(anId, aResource));
            case VIDEO -> aVideo.setVideo(this.mediaResourceGateway.storeAudioVideo(anId, aResource));
            case TRAILER -> aVideo.setTrailer(this.mediaResourceGateway.storeAudioVideo(anId, aResource));
        }

        return UploadMediaOuput.with(
                videoGateway.update(aVideo),
                aResource.type()
        );
    }

    private NotFoundException notFound(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
