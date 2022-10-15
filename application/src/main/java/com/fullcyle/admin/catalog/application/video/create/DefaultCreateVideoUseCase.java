package com.fullcyle.admin.catalog.application.video.create;

import com.fullcyle.admin.catalog.domain.Identifier;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.DomainException;
import com.fullcyle.admin.catalog.domain.exceptions.InternalErrorException;
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

import static java.util.Objects.requireNonNull;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultCreateVideoUseCase(final CategoryGateway categoryGateway,
                                     final GenreGateway genreGateway,
                                     final CastMemberGateway castMemberGateway,
                                     final VideoGateway videoGateway,
                                     final MediaResourceGateway mediaResourceGateway) {
        this.categoryGateway = requireNonNull(categoryGateway);
        this.genreGateway = requireNonNull(genreGateway);
        this.castMemberGateway = requireNonNull(castMemberGateway);
        this.videoGateway = requireNonNull(videoGateway);
        this.mediaResourceGateway = mediaResourceGateway;
    }


    @Override
    public CreateVideoOutput execute(final CreateVideoCommand aCommand) {
        final var aRating = Rating.of(aCommand.rating()).orElseThrow(invalidRating(aCommand.rating()));
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var members = toIdentifier(aCommand.members(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));


        final var aVideo = Video.newVideo(
                aCommand.title(),
                aCommand.description(),
                Year.of(aCommand.lauchedAt()),
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
            throw new NotificationException("Could not create Agrregate Video", notification);
        }

        return CreateVideoOutput.from(create(aCommand, aVideo));
    }

    private Video create(final CreateVideoCommand aCommand, final Video aVideo) {
        final var anId = aVideo.getId();
        try {
            Function<Resource, AudioVideoMedia> storeAudioVideo = it -> this.mediaResourceGateway.storeAudioVideo(anId, it);
            Function<Resource, ImageMedia> storeImage = it -> this.mediaResourceGateway.storeImage(anId, it);

            final var aVideoMedia = aCommand.getVideo()
                    .map(storeAudioVideo)
                    .orElse(null);

            final var aTrailerMedia = aCommand.getTrailer()
                    .map(storeAudioVideo)
                    .orElse(null);

            final var aBannerMedia = aCommand.getBanner()
                    .map(storeImage)
                    .orElse(null);

            final var aThumbnailMedia = aCommand.getThumbnail()
                    .map(storeImage)
                    .orElse(null);

            final var aThumbHalfMedia = aCommand.getThumbnailHalf()
                    .map(storeImage)
                    .orElse(null);


            return this.videoGateway.create(
                    aVideo
                            .setVideo(aVideoMedia)
                            .setTrailer(aTrailerMedia)
                            .setBanner(aBannerMedia)
                            .setThumbnail(aThumbnailMedia)
                            .setThumbnailHalf(aThumbHalfMedia)
            );
        } catch (final Throwable t) {
            this.mediaResourceGateway.clearResources(anId);
            throw InternalErrorException.with("An error on create video was observed [videoId: %s]".formatted(anId.getValue()), t);
        }
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

    private Supplier<DomainException> invalidRating(final String rating) {
        return () -> DomainException.with(new Error("Rating not found %s".formatted(rating)));
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }
}
