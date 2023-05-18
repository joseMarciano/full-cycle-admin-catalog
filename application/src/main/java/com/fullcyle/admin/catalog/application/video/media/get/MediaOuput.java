package com.fullcyle.admin.catalog.application.video.media.get;

import com.fullcyle.admin.catalog.domain.video.Resource;

public record MediaOuput(
        String name,
        String contentType,
        byte[] content
) {

    public static MediaOuput with(final Resource aResource) {
        return new MediaOuput(
                aResource.name(),
                aResource.contentType(),
                aResource.content()
        );
    }
}
