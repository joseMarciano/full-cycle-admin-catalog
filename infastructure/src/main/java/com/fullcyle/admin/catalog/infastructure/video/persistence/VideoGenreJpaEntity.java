package com.fullcyle.admin.catalog.infastructure.video.persistence;

import com.fullcyle.admin.catalog.domain.genre.GenreID;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "VIDEOS_GENRES")
@Entity(name = "VideoGenre")
public class VideoGenreJpaEntity {

    @EmbeddedId
    private VideoGenreId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    @JoinColumn(name = "I_VIDEOS")
    private VideoJpaEntity video;

    public VideoGenreJpaEntity() {
    }

    private VideoGenreJpaEntity(final VideoGenreId id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoGenreJpaEntity from(final VideoJpaEntity video, final GenreID genreId) {
        return new VideoGenreJpaEntity(
                VideoGenreId.from(video.getId(), genreId.getValue()),
                video
        );
    }

    public VideoGenreId getId() {
        return id;
    }

    public void setId(final VideoGenreId id) {
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
        final VideoGenreJpaEntity that = (VideoGenreJpaEntity) o;
        return getId().equals(that.getId()) && getVideo().equals(that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }
}
