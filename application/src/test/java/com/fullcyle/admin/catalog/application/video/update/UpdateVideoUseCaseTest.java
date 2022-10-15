package com.fullcyle.admin.catalog.application.video.update;

import com.fullcyle.admin.catalog.application.Fixture;
import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.InternalErrorException;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
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
import static org.mockito.Mockito.*;

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
    public void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() {

        final var aVideo = Fixture.Videos.video();

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
        verify(videoGateway).findById(eq(aVideo.getId()));

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

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsUpdateVideo_shouldReturnVideoId() {

        final var aVideo = Fixture.Videos.video();

        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of();
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
        verify(categoryGateway, times(0)).existsByIds(eq(expectedCategories));
        verify(videoGateway).findById(eq(aVideo.getId()));


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

    @Test
    public void givenAValidCommandWithoutGenres_whenCallsUpdateVideo_shouldReturnVideoId() {
        final var aVideo = Fixture.Videos.video();
        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.of(Categories.aulas().getId());
        final var expectedGenres = Set.<GenreID>of();
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
        verify(genreGateway, times(0)).existsByIds(eq(expectedGenres));
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

    @Test
    public void givenAValidCommandWithoutMembers_whenCallsUpdateVideo_shouldReturnVideoId() {
        final var aVideo = Fixture.Videos.video();
        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.of(Categories.aulas().getId());
        final var expectedGenres = Set.of(Genres.tech().getId());
        final var expectedMembers = Set.<CastMemberID>of();
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


        when(videoGateway.update(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        mockImageMedia();
        mockAudioVideoMedia();


        final var actualResult = useCase.execute(aCommand);


        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());


        verify(castMemberGateway, times(0)).existsByIds(eq(expectedMembers));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(categoryGateway).existsByIds(eq(expectedCategories));
        verify(videoGateway).findById(eq(aVideo.getId()));

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

    @Test
    public void givenAValidCommandWithoutResources_whenCallsUpdateVideo_shouldReturnVideoId() {
        final var aVideo = Fixture.Videos.video();
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
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

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


        final var actualResult = useCase.execute(aCommand);


        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(categoryGateway).existsByIds(eq(expectedCategories));
        verify(videoGateway).findById(eq(aVideo.getId()));

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
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    public void givenNullTitle_whenCallsUpdateVideo_shouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();

        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;
        final String expectedTitle = null;
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

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

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(videoGateway).findById(eq(aVideo.getId()));
        verify(videoGateway, times(0)).create(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(categoryGateway, times(0)).existsByIds(any());
    }

    @Test
    public void givenEmptyTitle_whenCallsUpdateVideo_shouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedTitle = "";
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

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

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(videoGateway, times(0)).create(any());
        verify(castMemberGateway, times(0)).existsByIds(eq(expectedMembers));
        verify(genreGateway, times(0)).existsByIds(eq(expectedGenres));
        verify(categoryGateway, times(0)).existsByIds(eq(expectedCategories));
    }

    @Test
    public void givenNullRating_whenCallsUpdateVideo_shouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();

        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
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

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(videoGateway, times(0)).create(any());
        verify(castMemberGateway, times(0)).existsByIds(eq(expectedMembers));
        verify(genreGateway, times(0)).existsByIds(eq(expectedGenres));
        verify(categoryGateway, times(0)).existsByIds(eq(expectedCategories));
    }

    @Test
    public void givenAInvalidRating_whenCallsUpdateVideo_shouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();

        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = "423489jkfhjks";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = UpdateVideoCommand.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
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

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(videoGateway, times(0)).create(any());
        verify(castMemberGateway, times(0)).existsByIds(eq(expectedMembers));
        verify(genreGateway, times(0)).existsByIds(eq(expectedGenres));
        verify(categoryGateway, times(0)).existsByIds(eq(expectedCategories));
    }

    @Test
    public void givenNullLaunchYear_whenCallsUpdateVideo_shouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();

        final var expectedErrorMessage = "'lauchedAt' should not be null";
        final var expectedErrorCount = 1;
        final var expectedTitle = title();
        final var expectedDescription = description();
        final Year expectedLaunchYear = null;
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

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

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(videoGateway, times(0)).create(any());
        verify(castMemberGateway, times(0)).existsByIds(eq(expectedMembers));
        verify(genreGateway, times(0)).existsByIds(eq(expectedGenres));
        verify(categoryGateway, times(0)).existsByIds(eq(expectedCategories));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideoAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();

        final var actualInvalidCategoryId = Categories.aulas().getId();
        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(actualInvalidCategoryId.getValue());
        final var expectedErrorCount = 1;
        final var expectedTitle = title();
        final var expectedDescription = description();
        final Year expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.of(actualInvalidCategoryId);
        final var expectedGenres = Set.of(Genres.tech().getId());
        final var expectedMembers = Set.of(CastMembers.gabriel().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;


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

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));


        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(videoGateway, times(0)).create(any());
        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(categoryGateway).existsByIds(eq(expectedCategories));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideoAndSomeGenresDoesNotExists_shouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();

        final var actualInvalidGenresId = Genres.tech().getId();
        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(actualInvalidGenresId.getValue());
        final var expectedErrorCount = 1;
        final var expectedTitle = title();
        final var expectedDescription = description();
        final Year expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.of(Categories.aulas().getId());
        final var expectedGenres = Set.of(actualInvalidGenresId);
        final var expectedMembers = Set.of(CastMembers.gabriel().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;


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

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));


        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(videoGateway, times(0)).create(any());
        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(categoryGateway).existsByIds(eq(expectedCategories));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideoAndSomeCastMembersDoesNotExists_shouldReturnDomainException() {
        final var aVideo = Fixture.Videos.video();

        final var actualInvalidMembersId = CastMembers.wesley().getId();
        final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(actualInvalidMembersId.getValue());
        final var expectedErrorCount = 1;
        final var expectedTitle = title();
        final var expectedDescription = description();
        final Year expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.of(Categories.aulas().getId());
        final var expectedGenres = Set.of(Genres.tech().getId());
        final var expectedMembers = Set.of(actualInvalidMembersId);
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;


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

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());


        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        verify(videoGateway, times(0)).create(any());
        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(categoryGateway).existsByIds(eq(expectedCategories));
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeErrorOcurr_shouldReturnCallsClearResources() {
        final var aVideo = Fixture.Videos.video();

        final var expectedErrorMessage = "An error on update video was observed [videoId: ";
        final var expectedTitle = title();
        final var expectedDescription = description();
        final Year expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.of(Categories.aulas().getId());
        final var expectedGenres = Set.of(Genres.tech().getId());
        final var expectedMembers = Set.of(CastMembers.gabriel().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;


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

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(videoGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(InternalErrorException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertTrue(actualException.getMessage().startsWith(expectedErrorMessage));
        verify(videoGateway, times(1)).update(any());
        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(mediaResourceGateway, times(0)).clearResources(any());
        verify(categoryGateway).existsByIds(eq(expectedCategories));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideoAndVideoDoesntExists_shouldReturnNotFoundException() {
        final var expectedErrorMessage = "Video with ID 123 was not found";
        final var expectedTitle = title();
        final var expectedDescription = description();
        final Year expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.of(Categories.aulas().getId());
        final var expectedGenres = Set.of(Genres.tech().getId());
        final var expectedMembers = Set.of(CastMembers.gabriel().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;


        final var aCommand = UpdateVideoCommand.with(
                VideoID.from("123").getValue(),
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
                .thenReturn(Optional.empty());

        final var actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(videoGateway, times(0)).update(any());
        verify(castMemberGateway, times(0)).existsByIds(eq(expectedMembers));
        verify(genreGateway, times(0)).existsByIds(eq(expectedGenres));
        verify(categoryGateway, times(0)).existsByIds(eq(expectedCategories));
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
