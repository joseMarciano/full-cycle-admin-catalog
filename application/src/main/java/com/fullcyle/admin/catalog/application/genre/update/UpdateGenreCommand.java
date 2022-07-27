package com.fullcyle.admin.catalog.application.genre.update;

import java.util.List;

import static java.util.Optional.ofNullable;

public record UpdateGenreCommand(
        String id,
        String name,
        boolean isActive,
        List<String> categories
) {

    public static UpdateGenreCommand with(final String id,
                                          final String name,
                                          final Boolean isActive,
                                          final List<String> categories) {
        return new UpdateGenreCommand(id,
                name,
                ofNullable(isActive).orElse(true),
                categories);
    }
}
