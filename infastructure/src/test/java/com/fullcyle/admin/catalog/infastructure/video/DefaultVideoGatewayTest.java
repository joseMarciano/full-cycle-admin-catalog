package com.fullcyle.admin.catalog.infastructure.video;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.Fixture;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.video.AudioVideoMedia;
import com.fullcyle.admin.catalog.domain.video.ImageMedia;
import com.fullcyle.admin.catalog.domain.video.Video;
import com.fullcyle.admin.catalog.domain.video.VideoID;
import com.fullcyle.admin.catalog.infastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;

import static com.fullcyle.admin.catalog.domain.Fixture.*;
import static com.fullcyle.admin.catalog.domain.Fixture.Videos.description;
import static com.fullcyle.admin.catalog.domain.Fixture.Videos.rating;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    public void testInjection() {
        assertNotNull(videoGateway);
        assertNotNull(castMemberGateway);
        assertNotNull(categoryGateway);
        assertNotNull(genreGateway);
        assertNotNull(videoRepository);
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        final var wesley = castMemberGateway.create(CastMembers.wesley());
        final var aulas = categoryGateway.create(Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final var expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");


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


        final var actualVideo = videoGateway.create(aVideo);
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());


        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(actualVideo.getVideo().get().name(), expectedVideo.name());
        assertEquals(actualVideo.getTrailer().get().name(), expectedTrailer.name());
        assertEquals(actualVideo.getBanner().get().name(), expectedBanner.name());
        assertEquals(actualVideo.getThumbnail().get().name(), expectedThumb.name());
        assertEquals(actualVideo.getThumbnailHalf().get().name(), expectedThumbHalf.name());


        final var persistedVideo =
                videoRepository.findById(actualVideo.getId().getValue())
                        .get();


        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear.getValue(), persistedVideo.getYearLaunched());
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        assertNotNull(persistedVideo.getCreatedAt());
        assertNotNull(persistedVideo.getUpdatedAt());
        assertEquals(persistedVideo.getVideo().getName(), expectedVideo.name());
        assertEquals(persistedVideo.getTrailer().getName(), expectedTrailer.name());
        assertEquals(persistedVideo.getBanner().getName(), expectedBanner.name());
        assertEquals(persistedVideo.getThumbnail().getName(), expectedThumb.name());
        assertEquals(persistedVideo.getThumbnailHalf().getName(), expectedThumbHalf.name());
    }

    @Test
    @Transactional
    public void givenAValidVideoWithOutRelations_whenCallsCreate_shouldPersistIt() {
        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

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
        );

        final var actualVideo = videoGateway.create(aVideo);
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());


        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertEquals(actualVideo.getUpdatedAt(), actualVideo.getCreatedAt());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());


        final var persistedVideo =
                videoRepository.findById(actualVideo.getId().getValue())
                        .get();


        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear.getValue(), persistedVideo.getYearLaunched());
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        assertNotNull(persistedVideo.getCreatedAt());
        assertEquals(persistedVideo.getUpdatedAt(), persistedVideo.getCreatedAt());
        assertNull(persistedVideo.getVideo());
        assertNull(persistedVideo.getTrailer());
        assertNull(persistedVideo.getBanner());
        assertNull(persistedVideo.getThumbnail());
        assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsUpdate_shouldPersistIt() {
        final var aVideo = videoGateway.create(Video.newVideo(
                title(),
                description(),
                Year.of(year()),
                duration(),
                bool(),
                bool(),
                rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        final var wesley = castMemberGateway.create(CastMembers.wesley());
        final var aulas = categoryGateway.create(Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final var expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var actualVideo = videoGateway.update(Video.with(aVideo).update(
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
                .setThumbnailHalf(expectedThumbHalf));

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(actualVideo.getId(), aVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(actualVideo.getUpdatedAt().isAfter(actualVideo.getCreatedAt()));
        assertEquals(actualVideo.getVideo().get().name(), expectedVideo.name());
        assertEquals(actualVideo.getTrailer().get().name(), expectedTrailer.name());
        assertEquals(actualVideo.getBanner().get().name(), expectedBanner.name());
        assertEquals(actualVideo.getThumbnail().get().name(), expectedThumb.name());
        assertEquals(actualVideo.getThumbnailHalf().get().name(), expectedThumbHalf.name());


        final var persistedVideo =
                videoRepository.findById(actualVideo.getId().getValue())
                        .get();


        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear.getValue(), persistedVideo.getYearLaunched());
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        assertNotNull(persistedVideo.getCreatedAt());
        assertTrue(persistedVideo.getUpdatedAt().isAfter(persistedVideo.getCreatedAt()));
        assertEquals(persistedVideo.getVideo().getName(), expectedVideo.name());
        assertEquals(persistedVideo.getTrailer().getName(), expectedTrailer.name());
        assertEquals(persistedVideo.getBanner().getName(), expectedBanner.name());
        assertEquals(persistedVideo.getThumbnail().getName(), expectedThumb.name());
        assertEquals(persistedVideo.getThumbnailHalf().getName(), expectedThumbHalf.name());
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsUpdateWithOutRelations_shouldPersistIt() {
        final var wesley = castMemberGateway.create(CastMembers.wesley());
        final var aulas = categoryGateway.create(Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());
        final var video = AudioVideoMedia.with("123", "video", "/media/video");
        final var trailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var banner = ImageMedia.with("123", "banner", "/media/banner");
        final var thumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var thumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var aVideo = videoGateway.create(Video.newVideo(
                        title(),
                        description(),
                        Year.of(year()),
                        duration(),
                        bool(),
                        bool(),
                        rating(),
                        Set.of(aulas.getId()),
                        Set.of(tech.getId()),
                        Set.of(wesley.getId())
                )).setVideo(video)
                .setTrailer(trailer)
                .setBanner(banner)
                .setThumbnail(thumb)
                .setThumbnailHalf(thumbHalf);


        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final AudioVideoMedia expectedVideo = null;
        final AudioVideoMedia expectedTrailer = null;
        final ImageMedia expectedBanner = null;
        final ImageMedia expectedThumb = null;
        final ImageMedia expectedThumbHalf = null;

        final var actualVideo = videoGateway.update(Video.with(aVideo).update(
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
                .setThumbnailHalf(expectedThumbHalf));

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(actualVideo.getId(), aVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchYear, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(actualVideo.getUpdatedAt().isAfter(actualVideo.getCreatedAt()));
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());


        final var persistedVideo =
                videoRepository.findById(actualVideo.getId().getValue())
                        .get();


        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear.getValue(), persistedVideo.getYearLaunched());
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.isOpened());
        assertEquals(expectedPublished, persistedVideo.isPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        assertEquals(expectedGenres, persistedVideo.getGenresID());
        assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        assertNotNull(persistedVideo.getCreatedAt());
        assertTrue(persistedVideo.getUpdatedAt().isAfter(persistedVideo.getCreatedAt()));
        assertNull(persistedVideo.getVideo());
        assertNull(persistedVideo.getTrailer());
        assertNull(persistedVideo.getBanner());
        assertNull(persistedVideo.getThumbnail());
        assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    public void givenAValidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        final var wesley = castMemberGateway.create(CastMembers.wesley());
        final var aulas = categoryGateway.create(Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final var expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");


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


        assertEquals(videoRepository.count(), 0L);

        final var actualVideo = videoGateway.create(aVideo);
        final var anId = actualVideo.getId();

        assertEquals(videoRepository.count(), 1L);

        videoGateway.deleteById(anId);

        assertEquals(videoRepository.count(), 0L);
    }

    @Test
    public void givenAnInvalidVideoId_whenCallsDeleteById_shouldBeOk() {
        final var wesley = castMemberGateway.create(CastMembers.wesley());
        final var aulas = categoryGateway.create(Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final var expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");


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


        assertEquals(videoRepository.count(), 0L);

        videoGateway.create(aVideo);
        final var anId = VideoID.unique();

        assertEquals(videoRepository.count(), 1L);

        videoGateway.deleteById(anId);

        assertEquals(videoRepository.count(), 1L);
    }

    @Test
    public void givenAValidVideoId_whenCallsFindById_shouldFindIt() {
        final var wesley = castMemberGateway.create(CastMembers.wesley());
        final var aulas = categoryGateway.create(Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = title();
        final var expectedDescription = description();
        final var expectedLaunchYear = Year.of(year());
        final var expectedDuration = duration();
        final var expectedOpened = bool();
        final var expectedPublished = bool();
        final var expectedRating = rating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final var expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final var expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final var expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final var expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final var expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");


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

        assertEquals(videoRepository.count(), 0L);

        final var actualVideo = videoGateway.create(aVideo);
        final var anId = actualVideo.getId();
        assertEquals(videoRepository.count(), 1L);

        final var persistedVideo = videoGateway.findById(anId).get();
        assertEquals(expectedTitle, persistedVideo.getTitle());
        assertEquals(expectedDescription, persistedVideo.getDescription());
        assertEquals(expectedLaunchYear, persistedVideo.getLauchedAt());
        assertEquals(expectedDuration, persistedVideo.getDuration());
        assertEquals(expectedOpened, persistedVideo.getOpened());
        assertEquals(expectedPublished, persistedVideo.getPublished());
        assertEquals(expectedRating, persistedVideo.getRating());
        assertEquals(expectedCategories, persistedVideo.getCategories());
        assertEquals(expectedGenres, persistedVideo.getGenres());
        assertEquals(expectedMembers, persistedVideo.getCastMembers());
        assertNotNull(persistedVideo.getCreatedAt());
        assertNotNull(persistedVideo.getUpdatedAt());
        assertEquals(persistedVideo.getVideo().get().name(), expectedVideo.name());
        assertEquals(persistedVideo.getTrailer().get().name(), expectedTrailer.name());
        assertEquals(persistedVideo.getBanner().get().name(), expectedBanner.name());
        assertEquals(persistedVideo.getThumbnail().get().name(), expectedThumb.name());
        assertEquals(persistedVideo.getThumbnailHalf().get().name(), expectedThumbHalf.name());
    }

    @Test
    public void givenAnInvalidVideoId_whenCallsFindById_shouldReturnEmpty() {
        final var anId = VideoID.unique();
        assertTrue(videoGateway.findById(anId).isEmpty());
    }

}
