package com.fullcyle.admin.catalog.infastructure.video;

import com.fullcyle.admin.catalog.domain.Identifier;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;
import com.fullcyle.admin.catalog.domain.video.VideoID;
import com.fullcyle.admin.catalog.domain.video.VideoPreview;
import com.fullcyle.admin.catalog.infastructure.utils.SQLUtils;
import com.fullcyle.admin.catalog.infastructure.video.persistence.VideoJpaEntity;
import com.fullcyle.admin.catalog.infastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    public DefaultVideoGateway(final VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }


    @Override
    public void deleteById(final VideoID anID) {
        final var aVideoId = anID.getValue();
        if (this.videoRepository.existsById(aVideoId)) {
            this.videoRepository.deleteById(aVideoId);
        }
    }

    @Override
    public Optional<Video> findById(final VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                SQLUtils.like(aQuery.terms()),
                toString(aQuery.castMembers()),
                toString(aQuery.categories()),
                toString(aQuery.genres()),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private static Set<String> toString(final Set<? extends Identifier> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return null;
        }

        return ids.stream().map(Identifier::getValue).collect(Collectors.toSet());
    }

    private Video save(final Video aVideo) {
        return this.videoRepository.save(VideoJpaEntity.from(aVideo))
                .toAggregate();
    }
}
