package com.fullcyle.admin.catalog.infastructure.video.persistence;

import com.fullcyle.admin.catalog.domain.category.CategoryID;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Table(name = "VIDEOS_CATEGORIES")
@Entity(name = "VideoCategory")
public class VideoCategoryJpaEntity {

    @EmbeddedId
    private VideoCategoryId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("I_VIDEOS")
    private VideoJpaEntity video;

    public VideoCategoryJpaEntity() {
    }

    private VideoCategoryJpaEntity(final VideoCategoryId id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCategoryJpaEntity from(final VideoJpaEntity video, CategoryID categoryID) {
        return new VideoCategoryJpaEntity(
                VideoCategoryId.from(video.getId(), UUID.fromString(categoryID.getValue())),
                video
        );
    }

    public VideoCategoryId getId() {
        return id;
    }

    public void setId(final VideoCategoryId id) {
        this.id = id;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public void setVideo(final VideoJpaEntity video) {
        this.video = video;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
        return id.equals(that.id) && video.equals(that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }
}
