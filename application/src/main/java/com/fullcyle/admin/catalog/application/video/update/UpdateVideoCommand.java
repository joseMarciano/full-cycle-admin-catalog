package com.fullcyle.admin.catalog.application.video.update;

import com.fullcyle.admin.catalog.domain.video.Resource;

import java.time.Year;
import java.util.Optional;
import java.util.Set;

public record UpdateVideoCommand(
        String id,
        String title,
        String description,
        Integer lauchedAt,
        Double duration,
        Boolean opened,
        Boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        Resource video,
        Resource trailer,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf
) {
    public static UpdateVideoCommand with(
            final String anId,
            final String aTitle,
            final String aDescription,
            final Year launchYear,
            final Double aDuration,
            final Boolean opened,
            final Boolean published,
            final String aRating,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members,
            final Resource aVideo,
            final Resource aTrailer,
            final Resource aBanner,
            final Resource aThumb,
            final Resource aThumbHalf
    ) {

        return new UpdateVideoCommand(
                anId,
                aTitle,
                aDescription,
                launchYear != null ? launchYear.getValue() : null,
                aDuration,
                opened,
                published,
                aRating,
                categories,
                genres,
                members,
                aVideo,
                aTrailer,
                aBanner,
                aThumb,
                aThumbHalf
        );

    }

    public static UpdateVideoCommand with(final String id,
                                          final String title,
                                          final String description,
                                          final Year launchedYear,
                                          final Double duration,
                                          final Boolean opened,
                                          final Boolean published,
                                          final String rating,
                                          final Set<String> categories,
                                          final Set<String> genres,
                                          final Set<String> castMembers) {
        return with(
                id,
                title,
                description,
                launchedYear,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                castMembers
        );
    }

    public Optional<Resource> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<Resource> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<Resource> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<Resource> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<Resource> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }
}
