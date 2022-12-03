package com.fullcyle.admin.catalog.domain.pagination;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<CastMemberID> castMembers,
        Set<CastMemberID> categories,
        Set<CastMemberID> genres
) {
}
