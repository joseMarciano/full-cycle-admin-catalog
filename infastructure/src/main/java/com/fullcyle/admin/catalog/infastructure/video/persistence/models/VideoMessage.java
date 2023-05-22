package com.fullcyle.admin.catalog.infastructure.video.persistence.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMessage(
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}