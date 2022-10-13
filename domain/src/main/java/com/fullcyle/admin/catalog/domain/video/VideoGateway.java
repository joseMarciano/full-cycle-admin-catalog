package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video aVideo);

    void deleteById(VideoID anID);

    Optional<Video> findById(VideoID anId);

    Video update(Video aVideo);

    Pagination<Video> findAll(SearchQuery aQuery);
}
