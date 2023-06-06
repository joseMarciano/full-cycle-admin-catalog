package com.fullcyle.admin.catalog.infastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVideoResponse(
        @JsonProperty("id") String id
) {
}