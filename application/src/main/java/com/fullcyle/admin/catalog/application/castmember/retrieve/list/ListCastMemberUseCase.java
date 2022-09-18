package com.fullcyle.admin.catalog.application.castmember.retrieve.list;

import com.fullcyle.admin.catalog.application.UseCase;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;

public sealed abstract class ListCastMemberUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits DefaultListCastMemberUseCase {
}
