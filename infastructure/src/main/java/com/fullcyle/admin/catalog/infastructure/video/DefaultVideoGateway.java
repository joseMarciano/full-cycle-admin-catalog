package com.fullcyle.admin.catalog.infastructure.video;

import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.VideoSearchQuery;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;
import com.fullcyle.admin.catalog.domain.video.VideoID;
import com.fullcyle.admin.catalog.infastructure.video.persistence.VideoJpaEntity;
import com.fullcyle.admin.catalog.infastructure.video.persistence.VideoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Pagination<Video> findAll(final VideoSearchQuery aQuery) {
        return null;
    }

    private Video save(final Video aVideo) {
        return this.videoRepository.save(VideoJpaEntity.from(aVideo))
                .toAggregate();
    }
}
