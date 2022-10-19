package com.fullcyle.admin.catalog.infastructure.video.persistence;

import com.fullcyle.admin.catalog.domain.video.ImageMedia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "IMAGES_MEDIA")
@Entity
public class ImageMediaJpaEntity {

    @Id
    @Column(name = "ID", updatable = false)
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;

    public ImageMediaJpaEntity() {
    }

    private ImageMediaJpaEntity(final String id,
                                final String name,
                                final String filePath) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
    }

    public static ImageMediaJpaEntity from(final ImageMedia anImageMedia) {
        return new ImageMediaJpaEntity(
                anImageMedia.checksum(),
                anImageMedia.name(),
                anImageMedia.location()
        );
    }

    public ImageMedia toDomain() {
        return ImageMedia.with(
                this.getId(),
                this.getName(),
                this.getFilePath()
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
}
