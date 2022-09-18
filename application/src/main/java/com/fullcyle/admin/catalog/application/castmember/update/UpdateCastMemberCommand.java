package com.fullcyle.admin.catalog.application.castmember.update;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;

public record UpdateCastMemberCommand(
        String id,
        String name,
        CastMemberType type
) {

    public static UpdateCastMemberCommand with(final String id, final String aName, final CastMemberType type) {
        return new UpdateCastMemberCommand(id, aName, type);
    }


}
