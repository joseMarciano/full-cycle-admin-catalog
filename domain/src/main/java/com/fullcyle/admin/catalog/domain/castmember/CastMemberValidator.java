package com.fullcyle.admin.catalog.domain.castmember;

import com.fullcyle.admin.catalog.domain.validation.Error;
import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcyle.admin.catalog.domain.validation.Validator;

public class CastMemberValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 1;
    private final CastMember castMember;

    public CastMemberValidator(final ValidationHandler anHandler, final CastMember aMember) {
        super(anHandler);
        this.castMember = aMember;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.castMember.getName();

        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 1 and 255 characters"));
        }

    }

    private void checkTypeConstraints() {
        final var type = this.castMember.getType();

        if (type == null) {
            this.validationHandler().append(new Error("'type' should not be null"));
            return;
        }

    }
}
