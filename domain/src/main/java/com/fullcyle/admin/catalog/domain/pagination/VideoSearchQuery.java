package com.fullcyle.admin.catalog.domain.pagination;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
