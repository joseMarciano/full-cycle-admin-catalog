package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.validation.Error;
import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcyle.admin.catalog.domain.validation.Validator;

import static java.util.Objects.isNull;

public class VideoValidator extends Validator {

    private final Video video;

    protected VideoValidator(final Video video, final ValidationHandler anHandler) {
        super(anHandler);
        this.video = video;
    }

    @Override
    public void validate() {
        validateTitleConstraint();
        validateDescriptionConstraint();
        validateLauchedAtConstraint();
        validateRatingConstraint();
    }

    private void validateTitleConstraint() {
        final var MAX_NAME_SIZE = 255;
        final var title = this.video.getTitle();

        if (isNull(title)) {
            this.validationHandler().append(new Error("'title' should not be null"));
            return;
        }

        if (title.isEmpty()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
            return;
        }

        if (title.length() > MAX_NAME_SIZE) {
            this.validationHandler().append(new Error("'title' must be between 1 and 255 characters"));
        }
    }

    private void validateDescriptionConstraint() {
        final var MAX_DESCRIPTION_SIZE = 4000;
        final var description = this.video.getDescription();

        if (isNull(description)) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }

        if (description.isEmpty()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }

        if (description.length() > MAX_DESCRIPTION_SIZE) {
            this.validationHandler().append(new Error("'description' must be between 1 and 4000 characters"));
        }
    }

    private void validateLauchedAtConstraint() {
        final var lauchedAt = this.video.getLauchedAt();

        if (isNull(lauchedAt)) {
            this.validationHandler().append(new Error("'lauchedAt' should not be null"));
            return;
        }
    }

    private void validateRatingConstraint() {
        final var rating = this.video.getRating();

        if (isNull(rating)) {
            this.validationHandler().append(new Error("'rating' should not be null"));
            return;
        }
    }

}
