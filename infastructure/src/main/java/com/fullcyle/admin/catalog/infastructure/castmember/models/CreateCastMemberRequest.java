package com.fullcyle.admin.catalog.infastructure.castmember.models;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(
        String name,
        CastMemberType type
) {
}
