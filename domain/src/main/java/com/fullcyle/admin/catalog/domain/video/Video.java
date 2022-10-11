package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.AggregateRoot;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.utils.InstantUtils;
import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year lauchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;


    private Video(
            final VideoID anID,
            final String aTitle,
            final String aDescription,
            final Year aLauchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumb,
            final ImageMedia aThumbHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        super(anID);
        this.title = aTitle;
        this.description = aDescription;
        this.lauchedAt = aLauchYear;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.banner = aBanner;
        this.thumbnail = aThumb;
        this.thumbnailHalf = aThumbHalf;
        this.trailer = aTrailer;
        this.video = aVideo;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = members;
    }

    @Override
    public void validate(ValidationHandler handler) {

    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLauchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        final var anId = VideoID.unique();
        final var now = InstantUtils.now();
        return new Video(
                anId,
                aTitle,
                aDescription,
                aLauchYear,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                members
        );
    }

    public static Video with(
            final Video aVideo
    ) {
        return new Video(
                aVideo.getId(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLauchedAt(),
                aVideo.getDuration(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                aVideo.createdAt,
                aVideo.updatedAt,
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getVideo().orElse(null),
                new HashSet<>(aVideo.getCategories()),
                new HashSet<>(aVideo.getGenres()),
                new HashSet<>(aVideo.getCastMembers())
        );
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLauchedAt() {
        return lauchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean getOpened() {
        return opened;
    }

    public boolean getPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return ofNullable(thumbnailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return ofNullable(trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return ofNullable(video);
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : emptySet();
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? Collections.unmodifiableSet(castMembers) : emptySet();
    }

    public Video setTitle(final String title) {
        this.title = title;
        return this;
    }

    public Video setDescription(final String description) {
        this.description = description;
        return this;
    }

    public Video setLauchedAt(final Year lauchedAt) {
        this.lauchedAt = lauchedAt;
        return this;
    }

    public Video setDuration(final double duration) {
        this.duration = duration;
        return this;
    }

    public Video setRating(final Rating rating) {
        this.rating = rating;
        return this;
    }

    public Video setOpened(final boolean opened) {
        this.opened = opened;
        return this;
    }

    public Video setPublished(final boolean published) {
        this.published = published;
        return this;
    }

    public Video setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Video setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        return this;
    }

    public Video setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        return this;
    }

    public Video setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        return this;
    }

    public Video setVideo(final AudioVideoMedia video) {
        this.video = video;
        return this;
    }

    public Video setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : emptySet();
        return this;
    }

    public Video setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : emptySet();
        return this;
    }

    public Video setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers) : emptySet();
        return this;
    }
}
