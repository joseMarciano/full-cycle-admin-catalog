package com.fullcyle.admin.catalog.application.castmember.create;

import com.fullcyle.admin.catalog.domain.castmember.CastMember;

public record CreateCastMemberOutput(String id) {

    public static CreateCastMemberOutput from(final CastMember aMember) {
        return new CreateCastMemberOutput(aMember.getId().getValue());
    }


}
