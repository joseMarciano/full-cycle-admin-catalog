package com.fullcyle.admin.catalog.application.castmember.delete;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;

import java.util.Objects;

public final class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {

    private final CastMemberGateway castMeberGateway;

    public DefaultDeleteCastMemberUseCase(CastMemberGateway castMeberGateway) {
        this.castMeberGateway = Objects.requireNonNull(castMeberGateway);
    }

    @Override
    public void execute(String anId) {
        this.castMeberGateway.deleteById(CastMemberID.from(anId));
    }
}
