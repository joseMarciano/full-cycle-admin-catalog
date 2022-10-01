package com.fullcyle.admin.catalog.infastructure.castmember.models;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(
        String name,
        CastMemberType type
) {
}
