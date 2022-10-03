package com.fullcyle.admin.catalog.infastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberResponse(
        String id,
        String name,
        CastMemberType type,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {
}
