package com.fullcyle.admin.catalog.application.genre.create;

import com.fullcyle.admin.catalog.domain.genre.Genre;

public record CreateGenreOutput(String id) {

    public static CreateGenreOutput from(final String anId) {
        return new CreateGenreOutput(anId);
    }

    public static CreateGenreOutput from(final Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue());
    }
}
