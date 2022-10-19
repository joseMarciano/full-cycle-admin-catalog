package com.fullcyle.admin.catalog.infastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class VideoGenreId implements Serializable {

    @Column(name = "I_VIDEOS", nullable = false)
    private UUID videoId;

    @Column(name = "I_GENRES", nullable = false)
    private UUID genreId;

    public VideoGenreId() {
    }

    private VideoGenreId(final UUID videoId, final UUID genreId) {
        this.videoId = videoId;
        this.genreId = genreId;
    }

    public static VideoGenreId from(final UUID videoId, final UUID genreId) {
        return new VideoGenreId(videoId, genreId);
    }

    public UUID getVideoId() {
        return videoId;
    }

    public void setVideoId(final UUID videoId) {
        this.videoId = videoId;
    }

    public UUID getGenreId() {
        return genreId;
    }

    public void setGenreId(final UUID genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoGenreId that = (VideoGenreId) o;
        return videoId.equals(that.videoId) && genreId.equals(that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, genreId);
    }
}
