package com.fullcyle.admin.catalog.infastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoGenreId implements Serializable {

    @Column(name = "I_VIDEOS", nullable = false)
    private String videoId;

    @Column(name = "I_GENRES", nullable = false)
    private String genreId;

    public VideoGenreId() {
    }

    private VideoGenreId(final String videoId, final String genreId) {
        this.videoId = videoId;
        this.genreId = genreId;
    }

    public static VideoGenreId from(final String videoId, final String genreId) {
        return new VideoGenreId(videoId, genreId);
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(final String genreId) {
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
