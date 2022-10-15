package com.fullcyle.admin.catalog.application.video.retrieve.get;

import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.fullcyle.admin.catalog.application.Fixture.*;
import static com.fullcyle.admin.catalog.application.Fixture.Videos.description;
import static com.fullcyle.admin.catalog.application.Fixture.Videos.rating;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetVideo_shouldReturnIt() {
        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.of(Categories.aulas().getId());
        final var expectedGenres = Set.of(Genres.tech().getId());
        final var expectedMembers = Set.of(
                CastMembers.gabriel().getId(),
                CastMembers.wesley().getId()
        );
        final AudioVideoMedia expectedVideo = audioVideo(Resource.Type.VIDEO);
        final AudioVideoMedia expectedTrailer = audioVideo(Resource.Type.VIDEO);
        final ImageMedia expectedBanner = imageMedia(Resource.Type.BANNER);
        final ImageMedia expectedThumb = imageMedia(Resource.Type.THUMBNAIL);
        final ImageMedia expectedThumbHalf = imageMedia(Resource.Type.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var actualVideo = this.useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId.getValue(), actualVideo.id());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(asString(expectedCategories), actualVideo.categories());
        Assertions.assertEquals(asString(expectedGenres), actualVideo.genres());
        Assertions.assertEquals(asString(expectedMembers), actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbnailHalf());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.createdAt());
        Assertions.assertEquals(aVideo.getUpdatedAt(), actualVideo.updatedAt());

    }


    @Test
    public void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        final var expectedErrorMessage = "Video with ID 123 was not found";

        final var expectedID = VideoID.from("123");

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var actualError =
                Assertions.assertThrows(NotFoundException.class, () -> this.useCase.execute(expectedID.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualError.getMessage());

    }

    private AudioVideoMedia audioVideo(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return AudioVideoMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/videos/" + checksum,
                "",
                MediaStatus.PENDING
        );
    }

    private ImageMedia imageMedia(final Resource.Type type) {
        final var checksum = UUID.randomUUID().toString();
        return ImageMedia.with(
                checksum,
                type.name().toLowerCase(),
                "/images/" + checksum
        );
    }


}
