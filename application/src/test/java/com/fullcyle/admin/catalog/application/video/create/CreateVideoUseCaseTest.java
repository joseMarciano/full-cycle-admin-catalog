package com.fullcyle.admin.catalog.application.video.create;

import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.video.Resource;
import com.fullcyle.admin.catalog.domain.video.Resource.Type;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.fullcyle.admin.catalog.application.Fixture.*;
import static com.fullcyle.admin.catalog.application.Fixture.Videos.description;
import static com.fullcyle.admin.catalog.application.Fixture.Videos.rating;
import static org.mockito.Mockito.*;


public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
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
        final Resource expectedVideo = Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Videos.resource(Type.BANNER);
        final Resource expectedThumb = Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
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

        when(videoGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());


        final var actualResult = useCase.execute(aCommand);


        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(actualVideo ->

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
//                        && actualVideo.getVideo().isPresent()
//                        && actualVideo.getTrailer().isPresent()
//                        && actualVideo.getBanner().isPresent()
//                        && actualVideo.getThumbnail().isPresent()
//                        && actualVideo.getThumbnailHalf().isPresent()
        ));


    }

    @Override
    protected List<Object> getMocks() {
        return null;
    }
}
