package com.fullcyle.admin.catalog.application.video.retrieve.list;

import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {

    public static VideoListOutput from(final VideoPreview aVideo) {
        return new VideoListOutput(
                aVideo.id(),
                aVideo.title(),
                aVideo.description(),
                aVideo.createdAt(),
                aVideo.updatedAt()
        );
    }

    public record CategoryVideoListOutput(
            String id,
            String name
    ) {
        public static CategoryVideoListOutput from(final Category aCategory) {
            return new CategoryVideoListOutput(aCategory.getId().getValue(), aCategory.getName());
        }
    }
}
