package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.exceptions.DomainException;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        final String expectedTitle = null;
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
        final var expectedErrorCount = 1;
        final var expectedErrorDescription = "'title' should not be null";

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

        final var actualError = Assertions.assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertNotNull(actualError);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorDescription, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        final String expectedTitle = "";
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
        final var expectedErrorCount = 1;
        final var expectedErrorDescription = "'title' should not be empty";

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

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        final var actualError = Assertions.assertThrows(DomainException.class, validator::validate);

        assertNotNull(actualError);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorDescription, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
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
        final var expectedErrorCount = 1;
        final var expectedErrorDescription = "'title' must be between 1 and 255 characters";

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

        final var actualError = Assertions.assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertNotNull(actualError);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorDescription, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "";
        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorDescription = "'description' should not be empty";

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

        final var actualError = Assertions.assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertNotNull(actualError);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorDescription, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "System Design Interviews";
        final StringBuilder expectedDescription = new StringBuilder();
        for (int i = 0; i <= 4000; i++) {
            expectedDescription.append("A -");
        }

        final var expectedLauchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorDescription = "'description' must be between 1 and 4000 characters";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription.toString(),
                expectedLauchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var actualError = Assertions.assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertNotNull(actualError);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorDescription, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLauchedAt_whenCallsValidate_shouldReceiveError() {
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                These questions, duly considered, raise all doubts about the mobility
                of international capitals, challenging the ability to equalize the
                intended indices. Likewise, the phenomenon of the Internet is not mandatory
                for the analysis of the communication process as a whole. Above all,
                it is essential that consultation with the various activists can increase
                """;
        final Year expectedLauchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorDescription = "'lauchedAt' should not be null";

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

        final var actualError = Assertions.assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertNotNull(actualError);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorDescription, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidate_shouldReceiveError() {
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
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedErrorCount = 1;
        final var expectedErrorDescription = "'rating' should not be null";

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

        final var actualError = Assertions.assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        assertNotNull(actualError);
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorDescription, actualError.getErrors().get(0).message());
    }
}
