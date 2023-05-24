package com.fullcyle.admin.catalog.infastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("COMPLETED")
public record VideoEncoderCompleted(
        @JsonProperty("id") String id,
        @JsonProperty("output_bucket_path") String ouputBucket,
        @JsonProperty("video") VideoMetadata video


) implements VideoEncoderResult {
    private static final String COMPLETED = "COMPLETED";

    @Override
    public String getStatus() {
        return COMPLETED;
    }
}
