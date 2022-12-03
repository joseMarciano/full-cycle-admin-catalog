package com.fullcyle.admin.catalog.infastructure.utils;

import static java.util.Objects.isNull;

public final class SQLUtils {

    private SQLUtils() {
    }

    public static String like(final String term) {
        if (isNull(term)) return null;
        return "%" + term + "%";
    }
}
