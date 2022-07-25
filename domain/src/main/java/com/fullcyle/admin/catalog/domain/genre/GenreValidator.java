package com.fullcyle.admin.catalog.domain.genre;

import com.fullcyle.admin.catalog.domain.validation.Error;
import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcyle.admin.catalog.domain.validation.Validator;

public class GenreValidator extends Validator {
    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 1;

    private final Genre genre;

    public GenreValidator(final Genre genre, final ValidationHandler anHandler) {
        super(anHandler);
        this.genre = genre;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.genre.getName();

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
            this.validationHandler().append(new Error("'name' must be between " + NAME_MIN_LENGTH + " and " + NAME_MAX_LENGTH + " characters"));
        }

    }
}
