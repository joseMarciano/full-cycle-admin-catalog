package com.fullcyle.admin.catalog.infastructure.video.persistence;

import com.fullcyle.admin.catalog.domain.video.AudioVideoMedia;
import com.fullcyle.admin.catalog.domain.video.MediaStatus;

import javax.persistence.*;

@Table(name = "AUDIO_VIDEO_MEDIA")
@Entity(name = "AudioVideoMedia")
public class AudioVideoMediaJpaEntity {

    @Id
    @Column(name = "ID", updatable = false)
    private String id;

    @Column(name = "CHECKSUM", nullable = false)
    private String checksum;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;

    @Column(name = "ENCODED_PATH", nullable = false)
    private String encodedPath;

    @Column(name = "MEDIA_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaStatus status;

    public AudioVideoMediaJpaEntity() {
    }

    public AudioVideoMediaJpaEntity(final String anId,
                                    final String aChecksum,
                                    final String aName,
                                    final String aFilePath,
                                    final String anEncodedPath,
                                    final MediaStatus aStatus
    ) {
        this.id = anId;
        this.checksum = aChecksum;
        this.name = aName;
        this.filePath = aFilePath;
        this.encodedPath = anEncodedPath;
        this.status = aStatus;
    }

    public static AudioVideoMediaJpaEntity from(final AudioVideoMedia audioVideoMedia) {
        return new AudioVideoMediaJpaEntity(
                audioVideoMedia.id(),
                audioVideoMedia.checksum(),
                audioVideoMedia.name(),
                audioVideoMedia.rawLocation(),
                audioVideoMedia.encodedLocation(),
                audioVideoMedia.status()
        );
    }

    public AudioVideoMedia toDomain() {
        return AudioVideoMedia.with(
                getId(),
                getChecksum(),
                getName(),
                getFilePath(),
                getEncodedPath(),
                getStatus()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(final String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(final MediaStatus status) {
        this.status = status;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(final String checksum) {
        this.checksum = checksum;
    }
}
