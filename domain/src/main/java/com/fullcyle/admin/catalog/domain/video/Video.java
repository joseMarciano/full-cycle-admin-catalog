package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.AggregateRoot;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.utils.InstantUtils;
import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.*;

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
        new VideoValidator(this, handler).validate();
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
        return new Video(
                anID,
                aTitle,
                aDescription,
                aLauchYear,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                aCreationDate,
                aUpdateDate,
                aBanner,
                aThumb,
                aThumbHalf,
                aTrailer,
                aVideo,
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


    public Video update(final String aTitle,
                        final String aDescription,
                        final Year aLaunchYear,
                        final double aDuration,
                        final boolean wasOpened,
                        final boolean wasPublished,
                        final Rating aRating,
                        final Set<CategoryID> categories,
                        final Set<GenreID> genres,
                        final Set<CastMemberID> members) {

        this.title = aTitle;
        this.description = aDescription;
        this.lauchedAt = aLaunchYear;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.updatedAt = InstantUtils.now();
        this.setCategories(categories)
                .setCastMembers(members)
                .setGenres(genres);
        return this;
    }

    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setVideo(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = InstantUtils.now();
        return this;
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


    private Video setCategories(final Set<CategoryID> categories) {
        this.categories = Objects.nonNull(categories) ? new HashSet<>(categories) : emptySet();
        return this;
    }

    private Video setGenres(final Set<GenreID> genres) {
        this.genres = Objects.nonNull(genres) ? new HashSet<>(genres) : emptySet();
        return this;
    }

    private Video setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = Objects.nonNull(castMembers) ? new HashSet<>(castMembers) : emptySet();
        return this;
    }
}
