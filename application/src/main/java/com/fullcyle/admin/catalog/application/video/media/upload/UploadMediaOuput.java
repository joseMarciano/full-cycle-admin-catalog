package com.fullcyle.admin.catalog.application.video.media.upload;

import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoMediaType;

public record UploadMediaOuput(
        String videoId,
        VideoMediaType mediaType
) {

    public static UploadMediaOuput with(final Video aVideo, final VideoMediaType aType) {
        return new UploadMediaOuput(
                aVideo.getId().getValue(),
                aType
        );
    }
}
