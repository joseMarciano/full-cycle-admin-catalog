package com.fullcyle.admin.catalog.infastructure.castmember.presenters;

import com.fullcyle.admin.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.fullcyle.admin.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.fullcyle.admin.catalog.infastructure.castmember.models.CastMemberListResponse;
import com.fullcyle.admin.catalog.infastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {

    static CastMemberResponse present(final CastMemberOutput output) {
        return new CastMemberResponse(
                output.id(),
                output.name(),
                output.type(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput output) {
        return new CastMemberListResponse(
                output.id(),
                output.name(),
                output.type(),
                output.createdAt()
        );
    }
}
