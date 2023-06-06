package com.fullcyle.admin.catalog.infastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcyle.admin.catalog.application.video.retrieve.list.VideoListOutput;

import java.time.Instant;

public record VideoListResponse(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("created_at") Instant createdAt
) {
    public static VideoListResponse from(final VideoListOutput videoListOutput) {
        return new VideoListResponse(
                videoListOutput.id(),
                videoListOutput.title(),
                videoListOutput.description(),
                videoListOutput.updatedAt(),
                videoListOutput.createdAt()
        );
    }
}
