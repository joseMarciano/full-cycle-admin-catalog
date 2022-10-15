package com.fullcyle.admin.catalog.domain.exceptions;

public class InternalErrorException extends NoStackTraceException {

    protected InternalErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static InternalErrorException with(final String message, final Throwable t) {
        return new InternalErrorException(message, t);
    }
}
