package com.fullcyle.admin.catalog.application.castmember.update;

import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;

import java.util.function.Supplier;

public final class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand aCommand) {
        final var anId = CastMemberID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        final var aCastMember =
                castMemberGateway.findById(anId)
                        .orElseThrow(notFound(anId));


        final var notification = Notification.create();

        notification.validate(() -> aCastMember.update(aName, aType));

        if (notification.hasErrors()) {
            notify(aCastMember, notification);
        }

        return UpdateCastMemberOutput.from(castMemberGateway.update(aCastMember));
    }

    private void notify(CastMember aCastMember, Notification notification) {
        throw new NotificationException("Could not update Aggregate CastMember %s".formatted(aCastMember.getId().getValue()), notification);
    }

    private Supplier<NotFoundException> notFound(CastMemberID anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }
}
