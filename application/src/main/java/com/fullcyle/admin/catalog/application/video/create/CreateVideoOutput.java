package com.fullcyle.admin.catalog.application.video.create;

import com.fullcyle.admin.catalog.domain.video.Video;

public record CreateVideoOutput(
        String id
) {

    public static CreateVideoOutput from(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}
