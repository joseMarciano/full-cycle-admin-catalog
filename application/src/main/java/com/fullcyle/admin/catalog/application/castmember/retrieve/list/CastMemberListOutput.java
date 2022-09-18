package com.fullcyle.admin.catalog.application.castmember.retrieve.list;

import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt
) {

    public static CastMemberListOutput from(final CastMember aCastMember) {
        return new CastMemberListOutput(aCastMember.getId().getValue(),
                aCastMember.getName(),
                aCastMember.getType(),
                aCastMember.getCreatedAt()
        );
    }
}
