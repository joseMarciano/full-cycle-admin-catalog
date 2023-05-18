package com.fullcyle.admin.catalog.application.video.media.upload;

import com.fullcyle.admin.catalog.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource
) {

    public static UploadMediaCommand with(final String videoId, final VideoResource aVideoResource) {
        return new UploadMediaCommand(videoId, aVideoResource);
    }
}
