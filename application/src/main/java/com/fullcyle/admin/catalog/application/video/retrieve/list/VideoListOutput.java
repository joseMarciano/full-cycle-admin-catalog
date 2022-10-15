package com.fullcyle.admin.catalog.application.video.retrieve.list;

import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.utils.CollectionUtils;
import com.fullcyle.admin.catalog.domain.video.Video;

import java.time.Instant;
import java.util.List;

public record VideoListOutput(
        String id,
        String title,
        String description,
        List<CategoryVideoListOutput> categories,
        Instant createdAt,
        Instant updatedAt
) {

    public static VideoListOutput from(final Video aVideo, final List<Category> categories) {
        return new VideoListOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                CollectionUtils.mapTo(categories, CategoryVideoListOutput::from),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
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
