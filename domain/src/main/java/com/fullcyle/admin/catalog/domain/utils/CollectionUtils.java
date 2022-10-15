package com.fullcyle.admin.catalog.domain.utils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <IN, OUT> Set<OUT> mapTo(final Set<IN> set, Function<IN, OUT> mapper) {
        return set.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <IN, OUT> List<OUT> mapTo(final List<IN> set, Function<IN, OUT> mapper) {
        return set.stream().map(mapper).toList();
    }
}
