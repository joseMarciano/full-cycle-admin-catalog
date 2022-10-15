package com.fullcyle.admin.catalog.application.video.update;

import com.fullcyle.admin.catalog.application.Fixture;
import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static com.fullcyle.admin.catalog.application.Fixture.*;
import static com.fullcyle.admin.catalog.application.Fixture.Videos.description;
import static com.fullcyle.admin.catalog.application.Fixture.Videos.rating;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(
                videoGateway,
                categoryGateway,
                genreGateway,
                castMemberGateway,
                mediaResourceGateway
        );
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {

        final var aVideo = Fixture.Videos.systemDesign();

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
        final Resource expectedVideo = Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumb = Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbHalf = Videos.resource(Resource.Type.THUMBNAIL_HALF);


        final var aCommand = UpdateVideoCommand.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf

        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(videoGateway.update(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        mockImageMedia();
        mockAudioVideoMedia();


        final var actualResult = useCase.execute(aCommand);


        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(categoryGateway).existsByIds(eq(expectedCategories));

        verify(videoGateway).update(argThat(actualVideo ->

                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLauchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && actualVideo.getVideo().isPresent()
                        && actualVideo.getTrailer().isPresent()
                        && actualVideo.getBanner().isPresent()
                        && actualVideo.getThumbnail().isPresent()
                        && actualVideo.getThumbnailHalf().isPresent()
                        && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
        ));
    }

    private void mockAudioVideoMedia() {
        when(mediaResourceGateway.storeImage(any(), any()))
                .thenAnswer(t -> {
                    final var resource = t.getArgument(1, Resource.class);
                    return ImageMedia.with(UUID.randomUUID().toString(), resource.name(), "/img");
                });
    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeAudioVideo(any(), any()))
                .thenAnswer(t -> {
                    final var resource = t.getArgument(1, Resource.class);
                    return AudioVideoMedia
                            .with(UUID.randomUUID().toString(), resource.name(), "/img", "", MediaStatus.PENDING);
                });
    }
}
