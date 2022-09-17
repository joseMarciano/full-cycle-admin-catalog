package com.fullcyle.admin.catalog.domain.castmember;

import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcyle.admin.catalog.domain.validation.Validator;

public class CastMemberValidator extends Validator {

    private final CastMember castMember;

    public CastMemberValidator(final ValidationHandler anHandler, final CastMember aMember) {
        super(anHandler);
        this.castMember = aMember;
    }

    @Override
    public void validate() {

    }
}
