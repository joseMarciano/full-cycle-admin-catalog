package com.fullcyle.admin.catalog.infastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

import static java.lang.Boolean.TRUE;

public record CreateGenreRequest(
        String name,
        @JsonProperty("categories_id") List<String> categories,
        @JsonProperty("is_active") Boolean active
) {

    public List<String> categories() {
        return this.categories != null ? this.categories : Collections.emptyList();
    }

    public boolean isActive() {
        return TRUE.equals(active);
    }
}
