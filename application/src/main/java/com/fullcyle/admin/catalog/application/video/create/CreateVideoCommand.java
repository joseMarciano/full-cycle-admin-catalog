package com.fullcyle.admin.catalog.application.video.create;

import com.fullcyle.admin.catalog.domain.video.Resource;

import java.time.Year;
import java.util.Set;

public record CreateVideoCommand(
        String title,
        String description,
        int lauchedAt,
        double duration,
        boolean opened,
        boolean published,
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
    public static CreateVideoCommand with(final String aTitle,
                                          final String aDescription,
                                          final Year launchYear,
                                          final Double aDuration,
                                          final boolean opened,
                                          final boolean published,
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

        return new CreateVideoCommand(
                aTitle,
                aDescription,
                launchYear.getValue(),
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
}
