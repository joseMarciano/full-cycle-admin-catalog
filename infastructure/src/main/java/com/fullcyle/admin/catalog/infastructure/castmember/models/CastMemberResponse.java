package com.fullcyle.admin.catalog.infastructure.castmember.models;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberResponse(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {
}
