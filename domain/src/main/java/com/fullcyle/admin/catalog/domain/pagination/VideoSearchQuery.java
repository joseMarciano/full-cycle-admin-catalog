package com.fullcyle.admin.catalog.domain.pagination;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.GenreID;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<CastMemberID> castMembers,
        Set<CategoryID> categories,
        Set<GenreID> genres
) {
}
