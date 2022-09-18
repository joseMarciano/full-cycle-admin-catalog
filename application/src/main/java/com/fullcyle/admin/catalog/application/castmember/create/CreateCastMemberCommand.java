package com.fullcyle.admin.catalog.application.castmember.create;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;

public record CreateCastMemberCommand(
        String name,
        CastMemberType type
) {

    public static CreateCastMemberCommand with(final String aName, final CastMemberType type) {
        return new CreateCastMemberCommand(aName, type);
    }


}
