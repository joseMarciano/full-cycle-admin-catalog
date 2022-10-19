package com.fullcyle.admin.catalog.infastructure.video.persistence;


import com.fullcyle.admin.catalog.domain.video.Rating;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoID;

import javax.persistence.*;
import java.time.Instant;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Table(name = "VIDEOS")
@Entity(name = "Video")
public class VideoJpaEntity {

    /**
     * The entity video will be persistent many times, for this reason we will map the Id like a
     * UUID and store the data in binary format instead of string, consequently taking up less disk space in
     * database
     */
    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", length = 4000)
    private String description;

    @Column(name = "YEAR", nullable = false)
    private int yearLaunched;

    @Column(name = "OPENED")
    private boolean opened;

    @Column(name = "PUBLISHED")
    private boolean published;

    @Column(name = "RATING")
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "DURATION", precision = 2)
    private double duration;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "I_VIDEOS")
    private AudioVideoMediaJpaEntity video;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "I_TRAILERS")
    private AudioVideoMediaJpaEntity trailer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "I_BANNERS")
    private ImageMediaJpaEntity banner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "I_THUMBNAILS")
    private ImageMediaJpaEntity thumbnail;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "I_THUMBNAILS_HALF")
    private ImageMediaJpaEntity thumbnailHalf;

    public VideoJpaEntity() {
    }

    public VideoJpaEntity(final UUID anId,
                          final String aTitle,
                          final String aDescription,
                          final int yearLaunched,
                          final boolean isOpened,
                          final boolean isPublished,
                          final Rating aRating,
                          final double aDuration,
                          final Instant createdAt,
                          final Instant updatedAt,
                          final AudioVideoMediaJpaEntity video,
                          final AudioVideoMediaJpaEntity trailer,
                          final ImageMediaJpaEntity banner,
                          final ImageMediaJpaEntity thumbnail,
                          final ImageMediaJpaEntity thumbnailHalf) {
        this.id = anId;
        this.title = aTitle;
        this.description = aDescription;
        this.yearLaunched = yearLaunched;
        this.opened = isOpened;
        this.published = isPublished;
        this.rating = aRating;
        this.duration = aDuration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
    }

    public static VideoJpaEntity from(final Video aVideo) {
        return new VideoJpaEntity(
                UUID.fromString(aVideo.getId().getValue()),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLauchedAt().getValue(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                aVideo.getDuration(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getVideo().map(AudioVideoMediaJpaEntity::from).orElse(null),
                aVideo.getTrailer().map(AudioVideoMediaJpaEntity::from).orElse(null),
                aVideo.getBanner().map(ImageMediaJpaEntity::from).orElse(null),
                aVideo.getThumbnail().map(ImageMediaJpaEntity::from).orElse(null),
                aVideo.getThumbnailHalf().map(ImageMediaJpaEntity::from).orElse(null)
        );
    }

    public Video toAggregate() {
        return Video.with(
                VideoID.from(getId()),
                getTitle(),
                getDescription(),
                Year.of(getYearLaunched()),
                getDuration(),
                isOpened(),
                isPublished(),
                getRating(),
                getCreatedAt(),
                getUpdatedAt(),
                Optional.ofNullable(getBanner()).map(ImageMediaJpaEntity::toDomain).orElse(null),
                Optional.ofNullable(getThumbnail()).map(ImageMediaJpaEntity::toDomain).orElse(null),
                Optional.of(getThumbnailHalf()).map(ImageMediaJpaEntity::toDomain).orElse(null),
                Optional.ofNullable(getTrailer()).map(AudioVideoMediaJpaEntity::toDomain).orElse(null),
                Optional.ofNullable(getVideo()).map(AudioVideoMediaJpaEntity::toDomain).orElse(null),
                null,
                null,
                null
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getYearLaunched() {
        return yearLaunched;
    }

    public void setYearLaunched(final int yearLaunched) {
        this.yearLaunched = yearLaunched;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(final boolean opened) {
        this.opened = opened;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(final boolean published) {
        this.published = published;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(final Rating rating) {
        this.rating = rating;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(final double duration) {
        this.duration = duration;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AudioVideoMediaJpaEntity getVideo() {
        return video;
    }

    public void setVideo(final AudioVideoMediaJpaEntity video) {
        this.video = video;
    }

    public AudioVideoMediaJpaEntity getTrailer() {
        return trailer;
    }

    public void setTrailer(final AudioVideoMediaJpaEntity trailer) {
        this.trailer = trailer;
    }

    public ImageMediaJpaEntity getBanner() {
        return banner;
    }

    public void setBanner(final ImageMediaJpaEntity banner) {
        this.banner = banner;
    }

    public ImageMediaJpaEntity getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(final ImageMediaJpaEntity thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageMediaJpaEntity getThumbnailHalf() {
        return thumbnailHalf;
    }

    public void setThumbnailHalf(final ImageMediaJpaEntity thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
    }
}
