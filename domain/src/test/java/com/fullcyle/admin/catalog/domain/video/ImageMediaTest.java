package com.fullcyle.admin.catalog.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class ImageMediaTest {

    @Test
    public void givenAValidParams_whenCallsNewImageMedia_shouldReturnInstance() {
        final var expectedCheckSum = "abc";
        final var expectedName = "Video.mp4";
        final var expectedLocation = "/123/videos";

        final var actualAudioVideo = ImageMedia.with(
                expectedCheckSum,
                expectedName,
                expectedLocation
        );

        assertEquals(actualAudioVideo.checksum(), expectedCheckSum);
        assertEquals(actualAudioVideo.name(), expectedName);
        assertEquals(actualAudioVideo.location(), expectedLocation);
    }

    @Test
    void givenTwoImageMediaWithSameChecksumAndLocation_shouldReturnTrue() {
        final var expectedCheckSum = "abc";
        final var expectedLocation = "/123/videos";

        final var audioVideoMediaOne = ImageMedia.with(
                expectedCheckSum,
                "Random",
                expectedLocation
        );

        final var audioVideoMediaTwo = ImageMedia.with(
                expectedCheckSum,
                "Sample",
                expectedLocation
        );

        assertEquals(audioVideoMediaOne, audioVideoMediaTwo);
        assertNotSame(audioVideoMediaOne, audioVideoMediaTwo);
    }

    @Test
    void givenInvalidParams_whenCallsWith_shouldReturnError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with(
                        null,
                        "Sample",
                        "/123/videos"
                )
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with(
                        "abc",
                        null,
                        "/123/videos"
                )
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with(
                        "abc",
                        "Sample",
                        null
                )
        );
    }
}