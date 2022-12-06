package com.fullcyle.admin.catalog.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class AudioVideoMediaTest {

    @Test
    public void givenAValidParams_whenCallsNewAudioVideoMedia_shouldReturnInstance() {
        final var expectedId = "123";
        final var expectedCheckSum = "abc";
        final var expectedName = "Video.mp4";
        final var expectedRawLocation = "/123/videos";
        final var expectedEncodedLocation = "dkfmsfdlkf==21239890832@#2423";
        final var expectedMediaStatus = MediaStatus.COMPLETED;

        final var actualAudioVideo = AudioVideoMedia.with(
                expectedId,
                expectedCheckSum,
                expectedName,
                expectedRawLocation,
                expectedEncodedLocation,
                expectedMediaStatus
        );

        assertEquals(actualAudioVideo.checksum(), expectedCheckSum);
        assertEquals(actualAudioVideo.name(), expectedName);
        assertEquals(actualAudioVideo.rawLocation(), expectedRawLocation);
        assertEquals(actualAudioVideo.encodedLocation(), expectedEncodedLocation);
        assertEquals(actualAudioVideo.status(), expectedMediaStatus);
    }

    @Test
    void givenTwoAudioVideoMediaWithSameChecksumAndRawLocation_shouldReturnTrue() {
        final var expectedId = "123";
        final var expectedCheckSum = "abc";
        final var expectedRawLocation = "/123/videos";
        final var expectedEncodedLocation = "dkfmsfdlkf==21239890832@#2423";
        final var expectedMediaStatus = MediaStatus.COMPLETED;

        final var audioVideoMediaOne = AudioVideoMedia.with(
                expectedId,
                expectedCheckSum,
                "Random",
                expectedRawLocation,
                expectedEncodedLocation,
                expectedMediaStatus
        );

        final var audioVideoMediaTwo = AudioVideoMedia.with(
                expectedId,
                expectedCheckSum,
                "Sample",
                expectedRawLocation,
                expectedEncodedLocation,
                expectedMediaStatus
        );

        assertEquals(audioVideoMediaOne, audioVideoMediaTwo);
        assertNotSame(audioVideoMediaOne, audioVideoMediaTwo);
    }

    @Test
    void givenInvalidParams_whenCallsWith_shouldReturnError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                AudioVideoMedia.with(
                        null,
                        "asAD",
                        "Sample",
                        "/123/videos",
                        "231sdssfe3ef",
                        MediaStatus.PROCESSING
                )
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                AudioVideoMedia.with(
                        "123",
                        null,
                        null,
                        "/123/videos",
                        "231sdssfe3ef",
                        MediaStatus.PROCESSING
                )
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                AudioVideoMedia.with(
                        "1234",
                        "abc",
                        "Sample",
                        null,
                        "231sdssfe3ef",
                        MediaStatus.PROCESSING
                )
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                AudioVideoMedia.with(
                        "123",
                        "abc",
                        "Sample",
                        "/123/videos",
                        null,
                        MediaStatus.PROCESSING
                )
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                AudioVideoMedia.with(
                        "123",
                        "abc",
                        "Sample",
                        "/123/videos",
                        "231sdssfe3ef",
                        null
                )
        );


    }
}