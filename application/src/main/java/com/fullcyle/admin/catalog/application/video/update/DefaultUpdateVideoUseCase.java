package com.fullcyle.admin.catalog.application.video.update;

import com.fullcyle.admin.catalog.domain.Identifier;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.InternalErrorException;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.validation.Error;
import com.fullcyle.admin.catalog.domain.validation.ValidationHandler;
import com.fullcyle.admin.catalog.domain.validation.handler.Notification;
import com.fullcyle.admin.catalog.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.fullcyle.admin.catalog.domain.utils.CollectionUtils.mapTo;
import static com.fullcyle.admin.catalog.domain.video.VideoMediaType.*;
import static java.util.Objects.requireNonNull;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUpdateVideoUseCase(final CategoryGateway categoryGateway,
                                     final GenreGateway genreGateway,
                                     final CastMemberGateway castMemberGateway,
                                     final VideoGateway videoGateway,
                                     final MediaResourceGateway mediaResourceGateway) {
        this.categoryGateway = requireNonNull(categoryGateway);
        this.genreGateway = requireNonNull(genreGateway);
        this.castMemberGateway = requireNonNull(castMemberGateway);
        this.videoGateway = requireNonNull(videoGateway);
        this.mediaResourceGateway = requireNonNull(mediaResourceGateway);
    }

    @Override
    public UpdateVideoOutput execute(final UpdateVideoCommand aCommand) {
        final var anId = VideoID.from(aCommand.id());
        final var aRating = Rating.of(aCommand.rating()).orElse(null);
        final var aLaunchYear = aCommand.lauchedAt() != null ? Year.of(aCommand.lauchedAt()) : null;
        final var categories = mapTo(aCommand.categories(), CategoryID::from);
        final var genres = mapTo(aCommand.genres(), GenreID::from);
        final var members = mapTo(aCommand.members(), CastMemberID::from);


        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));


        aVideo.update(
                aCommand.title(),
                aCommand.description(),
                aLaunchYear,
                aCommand.duration(),
                aCommand.opened(),
                aCommand.published(),
                aRating,
                categories,
                genres,
                members
        );

        aVideo.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Could not update Aggregate Video", notification);
        }


        return UpdateVideoOutput.from(update(aCommand, aVideo));
    }

    private Video update(final UpdateVideoCommand aCommand, final Video aVideo) {
        final var anId = aVideo.getId();
        try {

            final var aVideoMedia = aCommand.getVideo()
                    .map(storeAudioVideo(anId, VIDEO))
                    .orElse(null);

            final var aTrailerMedia = aCommand.getTrailer()
                    .map(storeAudioVideo(anId, TRAILER))
                    .orElse(null);

            final var aBannerMedia = aCommand.getBanner()
                    .map(storeImage(anId, BANNER))
                    .orElse(null);

            final var aThumbnailMedia = aCommand.getThumbnail()
                    .map(storeImage(anId, THUMBNAIL))
                    .orElse(null);

            final var aThumbHalfMedia = aCommand.getThumbnailHalf()
                    .map(storeImage(anId, THUMBNAIL_HALF))
                    .orElse(null);


            return this.videoGateway.update(
                    aVideo
                            .updateVideoMedia(aVideoMedia)
                            .updateTrailerMedia(aTrailerMedia)
                            .updateBannerMedia(aBannerMedia)
                            .updateThumbnailMedia(aThumbnailMedia)
                            .updateThumbnailHalfMedia(aThumbHalfMedia)
            );
        } catch (final Throwable t) {
            throw InternalErrorException.with("An error on update video was observed [videoId: %s]".formatted(anId.getValue()), t);
        }
    }

    private Function<Resource, AudioVideoMedia> storeAudioVideo(final VideoID anId, final VideoMediaType type) {
        return it -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(it, type));
    }

    private Function<Resource, ImageMedia> storeImage(final VideoID anId, final VideoMediaType type) {
        return it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, type));
    }


    private Supplier<NotFoundException> notFound(final VideoID anId) {
        return () -> NotFoundException.with(Video.class, anId);
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, this.categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids) {
        return validateAggregate("genres", ids, this.genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> ids) {
        return validateAggregate("cast members", ids, this.castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final String label,
            final Set<T> ids,
            Function<Iterable<T>, List<T>> existsBIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) return notification;

        final var retrievedIds = existsBIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage =
                    missingIds.stream()
                            .map(Identifier::getValue)
                            .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(label, missingIdsMessage)));

        }

        return notification;
    }
}
