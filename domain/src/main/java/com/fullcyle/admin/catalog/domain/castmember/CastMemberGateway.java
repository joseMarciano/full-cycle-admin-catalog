package com.fullcyle.admin.catalog.domain.castmember;

import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;

import java.util.Optional;

public interface CastMemberGateway {

    CastMember create(CastMember aCastMember);

    void deleteById(CastMemberID anID);

    Optional<CastMember> findById(CastMemberID anId);

    CastMember update(CastMember aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);
}
