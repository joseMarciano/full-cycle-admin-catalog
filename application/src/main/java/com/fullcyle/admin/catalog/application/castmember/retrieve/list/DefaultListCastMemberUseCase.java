package com.fullcyle.admin.catalog.application.castmember.retrieve.list;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;

import java.util.Objects;

public non-sealed class DefaultListCastMemberUseCase extends ListCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultListCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(SearchQuery anSearchQuery) {
        return this.castMemberGateway.findAll(anSearchQuery)
                .map(CastMemberListOutput::from);
    }
}
