package com.fullcyle.admin.catalog.infastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcyle.admin.catalog.domain.video.VideoMediaType;

public record UploadVideoResponse(
        @JsonProperty("id") String videoId,
        @JsonProperty("media_type") VideoMediaType mediaType
) {
}