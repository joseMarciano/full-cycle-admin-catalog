package com.fullcyle.admin.catalog.application.castmember.create;

import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;

public final class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        final var notification = Notification.create();
        final var aCastMember =
                notification.validate(() -> CastMember.newMember(aName, aType));


        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate CastMember", notification);
        }

        return CreateCastMemberOutput.from(castMemberGateway.create(aCastMember));
    }
}
