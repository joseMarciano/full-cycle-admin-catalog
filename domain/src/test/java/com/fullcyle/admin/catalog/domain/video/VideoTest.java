package com.fullcyle.admin.catalog.domain.video;


import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    @Test
    public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(actualVideo.getCreatedAt(), actualVideo.getUpdatedAt());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    @Test
    public void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                "Any title",
                "Any description",
                Year.of(1999),
                0.0,
                true,
                true,
                Rating.AGE_10,
                Set.of(),
                Set.of(),
                Set.of()
        );

        final var actualVideo = Video.with(aVideo)
                .update(
                        expectedTitle,
                        expectedDescription,
                        expectedLauchedAt,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                );

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetVideo_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aVideoMedia = AudioVideoMedia.with(
                "abc",
                "Video.mp4",
                "/123/videos"
        );

        final var actualVideo =
                Video.with(aVideo)
                        .updateVideoMedia(aVideoMedia);


        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertEquals(aVideoMedia, actualVideo.getVideo().get());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    @Test
    public void givenValidVideo_whenCallsSetTrailer_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aTrailer = AudioVideoMedia.with(
                "abc",
                "Video.mp4",
                "/123/videos"
        );

        final var actualVideo =
                Video.with(aVideo)
                        .updateTrailerMedia(aTrailer);


        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertEquals(aTrailer, actualVideo.getTrailer().get());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetBanner_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aBanner = ImageMedia.with(
                "abc",
                "Image.jpg",
                "/123/images"
        );

        final var actualVideo =
                Video.with(aVideo)
                        .updateBannerMedia(aBanner);


        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertEquals(aBanner, actualVideo.getBanner().get());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    @Test
    public void givenValidVideo_whenCallsSetThumbNail_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aThumbNail = ImageMedia.with(
                "abc",
                "Image.jpg",
                "/123/images"
        );

        final var actualVideo =
                Video.with(aVideo)
                        .updateThumbnailMedia(aThumbNail);


        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertEquals(aThumbNail, actualVideo.getThumbnail().get());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    @Test
    public void givenValidVideo_whenCallsSetThumbNailHalf_shouldReturnUpdated() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aThumbNailHalf = ImageMedia.with(
                "abc",
                "Image.jpg",
                "/123/images"
        );

        final var actualVideo =
                Video.with(aVideo)
                        .updateThumbnailHalfMedia(aThumbNailHalf);


        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getId(), actualVideo.getId());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLauchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertEquals(aThumbNailHalf, actualVideo.getThumbnailHalf().get());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

}
