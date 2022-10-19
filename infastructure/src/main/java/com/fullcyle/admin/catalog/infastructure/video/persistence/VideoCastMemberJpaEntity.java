package com.fullcyle.admin.catalog.infastructure.video.persistence;

import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Table(name = "VIDEOS_CAST_MEMBERS")
@Entity(name = "VideoCastMember")
public class VideoCastMemberJpaEntity {

    @EmbeddedId
    private VideoCastMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("I_VIDEOS")
    private VideoJpaEntity video;

    public VideoCastMemberJpaEntity() {
    }

    private VideoCastMemberJpaEntity(final VideoCastMemberId id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCastMemberJpaEntity from(final VideoJpaEntity video, final CastMemberID castMemberID) {
        return new VideoCastMemberJpaEntity(
                VideoCastMemberId.from(video.getId(), UUID.fromString(castMemberID.getValue())),
                video
        );
    }

    public VideoCastMemberId getId() {
        return id;
    }

    public void setId(final VideoCastMemberId id) {
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
        final VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
        return getId().equals(that.getId()) && getVideo().equals(that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }
}
