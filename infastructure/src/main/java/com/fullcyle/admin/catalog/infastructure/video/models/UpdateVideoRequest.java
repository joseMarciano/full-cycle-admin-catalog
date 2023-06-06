package com.fullcyle.admin.catalog.infastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record UpdateVideoRequest(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("duration") Double duration,
        @JsonProperty("year_lauched") Integer yearLauched,
        @JsonProperty("opened") Boolean opened,
        @JsonProperty("published") Boolean published,
        @JsonProperty("rating") String rating,
        @JsonProperty("cast_members") Set<String> castMembers,
        @JsonProperty("categories") Set<String> categories,
        @JsonProperty("genres") Set<String> genres
) {
}
