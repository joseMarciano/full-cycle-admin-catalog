package com.fullcyle.admin.catalog.infastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class VideoCategoryId implements Serializable {

    @Column(name = "I_VIDEOS", nullable = false)
    private UUID videoId;

    @Column(name = "I_CATEGORIES", nullable = false)
    private UUID categoryId;


    public VideoCategoryId() {
    }

    private VideoCategoryId(final UUID videoId, final UUID categoryId) {
        this.videoId = videoId;
        this.categoryId = categoryId;
    }

    public static VideoCategoryId from(final UUID aVideoId, final UUID aCategoryId) {
        return new VideoCategoryId(
                aVideoId,
                aCategoryId
        );
    }

    public UUID getVideoId() {
        return videoId;
    }

    public void setVideoId(final UUID videoId) {
        this.videoId = videoId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final UUID categoryId) {
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
