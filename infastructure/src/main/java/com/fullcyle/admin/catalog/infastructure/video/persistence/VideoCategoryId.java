package com.fullcyle.admin.catalog.infastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCategoryId implements Serializable {

    @Column(name = "I_VIDEOS", nullable = false)
    private String videoId;

    @Column(name = "I_CATEGORIES", nullable = false)
    private String categoryId;


    public VideoCategoryId() {
    }

    private VideoCategoryId(final String videoId, final String categoryId) {
        this.videoId = videoId;
        this.categoryId = categoryId;
    }

    public static VideoCategoryId from(final String aVideoId, final String aCategoryId) {
        return new VideoCategoryId(
                aVideoId,
                aCategoryId
        );
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoCategoryId that = (VideoCategoryId) o;
        return getVideoId().equals(that.getVideoId()) && getCategoryId().equals(that.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVideoId(), getCategoryId());
    }
}
