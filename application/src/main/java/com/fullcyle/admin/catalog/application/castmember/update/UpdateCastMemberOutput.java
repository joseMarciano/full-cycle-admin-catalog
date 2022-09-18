package com.fullcyle.admin.catalog.application.castmember.update;

import com.fullcyle.admin.catalog.domain.castmember.CastMember;

public record UpdateCastMemberOutput(String id) {

    public static UpdateCastMemberOutput from(final CastMember aMember) {
        return new UpdateCastMemberOutput(aMember.getId().getValue());
    }


}
