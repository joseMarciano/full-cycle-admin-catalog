package com.fullcyle.admin.catalog.infastructure.video;

import com.fullcyle.admin.catalog.domain.video.*;
import com.fullcyle.admin.catalog.infastructure.configuration.properties.StorageProperties;
import com.fullcyle.admin.catalog.infastructure.services.StorageService;
import org.springframework.stereotype.Component;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final StorageService storageService;
    private final String fileNamePattern;
    private final String locationPattern;

    public DefaultMediaResourceGateway(final StorageProperties storageProperties, final StorageService storageService) {
        this.fileNamePattern = storageProperties.getFilenamePattern();
        this.locationPattern = storageProperties.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource);
        final var resource = videoResource.resource();
        store(filePath, resource);
        return AudioVideoMedia.with(resource.checksum(), resource.name(), filePath);
    }

    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource);
        final var resource = videoResource.resource();
        store(filePath, resource);
        return ImageMedia.with(resource.checksum(), resource.name(), filePath);
    }

    @Override
    public void clearResources(final VideoID anId) {
        final var ids = this.storageService.list(folder(anId));
        this.storageService.deleteAll(ids);
    }

    private String filename(final VideoMediaType type) {
        return fileNamePattern.replace("{type}", type.name());
    }

    private String folder(final VideoID anId) {
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filePath(final VideoID anId, final VideoResource aResource) {
        return folder(anId)
                .concat("/")
                .concat(filename(aResource.type()));
    }

    private void store(final String filaPath, final Resource resource) {
        this.storageService.store(filaPath, resource);
    }
}
