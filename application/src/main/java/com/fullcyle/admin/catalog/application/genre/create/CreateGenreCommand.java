package com.fullcyle.admin.catalog.application.genre.create;

import java.util.List;

public record CreateGenreCommand(
        String name,
        boolean isActive,
        List<String> categories
) {


    public static CreateGenreCommand with(final String aName, final boolean isActive, List<String> categories) {
        return new CreateGenreCommand(aName, isActive, categories);
    }
}
