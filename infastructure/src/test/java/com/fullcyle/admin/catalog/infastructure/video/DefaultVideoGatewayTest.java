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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;

import static com.fullcyle.admin.catalog.domain.Fixture.*;
import static com.fullcyle.admin.catalog.domain.Fixture.Videos.description;
import static com.fullcyle.admin.catalog.domain.Fixture.Videos.rating;

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
        Assertions.assertNotNull(videoGateway);
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(videoRepository);
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
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());


        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLauchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertNotNull(actualVideo.getCreatedAt());
        Assertions.assertNotNull(actualVideo.getUpdatedAt());
        Assertions.assertEquals(actualVideo.getVideo().get().name(), expectedVideo.name());
        Assertions.assertEquals(actualVideo.getTrailer().get().name(), expectedTrailer.name());
        Assertions.assertEquals(actualVideo.getBanner().get().name(), expectedBanner.name());
        Assertions.assertEquals(actualVideo.getThumbnail().get().name(), expectedThumb.name());
        Assertions.assertEquals(actualVideo.getThumbnailHalf().get().name(), expectedThumbHalf.name());


        final var persistedVideo =
                videoRepository.findById(actualVideo.getId().getValue())
                        .get();


        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear.getValue(), persistedVideo.getYearLaunched());
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        Assertions.assertNotNull(persistedVideo.getCreatedAt());
        Assertions.assertNotNull(persistedVideo.getUpdatedAt());
        Assertions.assertEquals(persistedVideo.getVideo().getName(), expectedVideo.name());
        Assertions.assertEquals(persistedVideo.getTrailer().getName(), expectedTrailer.name());
        Assertions.assertEquals(persistedVideo.getBanner().getName(), expectedBanner.name());
        Assertions.assertEquals(persistedVideo.getThumbnail().getName(), expectedThumb.name());
        Assertions.assertEquals(persistedVideo.getThumbnailHalf().getName(), expectedThumbHalf.name());
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
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());


        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLauchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertNotNull(actualVideo.getCreatedAt());
        Assertions.assertEquals(actualVideo.getUpdatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());


        final var persistedVideo =
                videoRepository.findById(actualVideo.getId().getValue())
                        .get();


        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear.getValue(), persistedVideo.getYearLaunched());
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        Assertions.assertNotNull(persistedVideo.getCreatedAt());
        Assertions.assertEquals(persistedVideo.getUpdatedAt(), persistedVideo.getCreatedAt());
        Assertions.assertNull(persistedVideo.getVideo());
        Assertions.assertNull(persistedVideo.getTrailer());
        Assertions.assertNull(persistedVideo.getBanner());
        Assertions.assertNull(persistedVideo.getThumbnail());
        Assertions.assertNull(persistedVideo.getThumbnailHalf());
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

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(actualVideo.getId(), aVideo.getId());
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLauchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertNotNull(actualVideo.getCreatedAt());
        Assertions.assertTrue(actualVideo.getUpdatedAt().isAfter(actualVideo.getCreatedAt()));
        Assertions.assertEquals(actualVideo.getVideo().get().name(), expectedVideo.name());
        Assertions.assertEquals(actualVideo.getTrailer().get().name(), expectedTrailer.name());
        Assertions.assertEquals(actualVideo.getBanner().get().name(), expectedBanner.name());
        Assertions.assertEquals(actualVideo.getThumbnail().get().name(), expectedThumb.name());
        Assertions.assertEquals(actualVideo.getThumbnailHalf().get().name(), expectedThumbHalf.name());


        final var persistedVideo =
                videoRepository.findById(actualVideo.getId().getValue())
                        .get();


        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear.getValue(), persistedVideo.getYearLaunched());
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        Assertions.assertNotNull(persistedVideo.getCreatedAt());
        Assertions.assertTrue(persistedVideo.getUpdatedAt().isAfter(persistedVideo.getCreatedAt()));
        Assertions.assertEquals(persistedVideo.getVideo().getName(), expectedVideo.name());
        Assertions.assertEquals(persistedVideo.getTrailer().getName(), expectedTrailer.name());
        Assertions.assertEquals(persistedVideo.getBanner().getName(), expectedBanner.name());
        Assertions.assertEquals(persistedVideo.getThumbnail().getName(), expectedThumb.name());
        Assertions.assertEquals(persistedVideo.getThumbnailHalf().getName(), expectedThumbHalf.name());
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

        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(actualVideo.getId(), aVideo.getId());
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLauchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertNotNull(actualVideo.getCreatedAt());
        Assertions.assertTrue(actualVideo.getUpdatedAt().isAfter(actualVideo.getCreatedAt()));
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());


        final var persistedVideo =
                videoRepository.findById(actualVideo.getId().getValue())
                        .get();


        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear.getValue(), persistedVideo.getYearLaunched());
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        Assertions.assertNotNull(persistedVideo.getCreatedAt());
        Assertions.assertTrue(persistedVideo.getUpdatedAt().isAfter(persistedVideo.getCreatedAt()));
        Assertions.assertNull(persistedVideo.getVideo());
        Assertions.assertNull(persistedVideo.getTrailer());
        Assertions.assertNull(persistedVideo.getBanner());
        Assertions.assertNull(persistedVideo.getThumbnail());
        Assertions.assertNull(persistedVideo.getThumbnailHalf());
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


        Assertions.assertEquals(videoRepository.count(), 0L);

        final var actualVideo = videoGateway.create(aVideo);
        final var anId = actualVideo.getId();

        Assertions.assertEquals(videoRepository.count(), 1L);

        videoGateway.deleteById(anId);

        Assertions.assertEquals(videoRepository.count(), 0L);
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


        Assertions.assertEquals(videoRepository.count(), 0L);

        videoGateway.create(aVideo);
        final var anId = VideoID.unique();

        Assertions.assertEquals(videoRepository.count(), 1L);

        videoGateway.deleteById(anId);

        Assertions.assertEquals(videoRepository.count(), 1L);
    }


}
