package com.fullcyle.admin.catalog.domain.exceptions;

import com.fullcyle.admin.catalog.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    private final List<Error> errors;

    private DomainException(final String aMessage, List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException(anErrors != null ? anErrors.get(0).message() : "", anErrors);
    }

    public static DomainException with(final Error anError) {
        return DomainException.with(List.of(anError));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
