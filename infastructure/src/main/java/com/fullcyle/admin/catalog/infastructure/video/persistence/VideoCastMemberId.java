package com.fullcyle.admin.catalog.infastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCastMemberId implements Serializable {

    @Column(name = "I_VIDEOS", nullable = false)
    private String videoId;

    @Column(name = "I_CAST_MEMBERS", nullable = false)
    private String castMemberId;

    public VideoCastMemberId() {
    }

    private VideoCastMemberId(final String videoId, final String castMemberId) {
        this.videoId = videoId;
        this.castMemberId = castMemberId;
    }

    public static VideoCastMemberId from(final String videoId, final String castMemberId) {
        return new VideoCastMemberId(
                videoId,
                castMemberId
        );
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public String getCastMemberId() {
        return castMemberId;
    }

    public void setCastMemberId(final String castMemberId) {
        this.castMemberId = castMemberId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoCastMemberId that = (VideoCastMemberId) o;
        return videoId.equals(that.videoId) && castMemberId.equals(that.castMemberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, castMemberId);
    }
}
