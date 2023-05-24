package com.fullcyle.admin.catalog.infastructure.video.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "status"
)
@VideoResponseTypes
public sealed interface VideoEncoderResult
        permits VideoEncoderCompleted, VideoEncoderError {

    String getStatus();

}
