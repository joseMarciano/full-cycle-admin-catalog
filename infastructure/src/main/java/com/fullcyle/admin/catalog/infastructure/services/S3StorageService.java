package com.fullcyle.admin.catalog.infastructure.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fullcyle.admin.catalog.domain.video.Resource;
import com.fullcyle.admin.catalog.infastructure.configuration.properties.S3Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
@Profile({"development", "production"})
public class S3StorageService implements StorageService {

    private static Logger LOGGER = LoggerFactory.getLogger(S3StorageService.class);

    private final String bucketName;
    private final AmazonS3 amazonS3;

    public S3StorageService(final S3Properties s3Properties, final AmazonS3 amazonS3) {
        this.bucketName = s3Properties.getBucketName();
        this.amazonS3 = amazonS3;
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        final var objectsName = new DeleteObjectsRequest(this.bucketName)
                .withKeys(names.stream().map(DeleteObjectsRequest.KeyVersion::new).toList());

        amazonS3.deleteObjects(objectsName);
    }

    @Override
    public Optional<Resource> get(final String name) {
        try {
            final var s3Object = this.amazonS3.getObject(this.bucketName, name);
            final var content = getContent(s3Object);
            return of(Resource.with("COLOCAR CHECKSUM", content, s3Object.getObjectMetadata().getContentType(), s3Object.getKey()));
        } catch (AmazonS3Exception e) {
            LOGGER.error("Error on get {} in bucket {} message: {}", name, this.bucketName, e.getMessage());
            return empty();
        }
    }


    @Override
    public List<String> list(final String prefix) {
        final var objectListing = this.amazonS3.listObjects(this.bucketName, prefix);

        return objectListing.getObjectSummaries().stream().map(S3ObjectSummary::getKey).toList();
    }

    @Override
    public void store(final String name, final Resource resource) {
        final var metadata = new ObjectMetadata();
        metadata.setContentType(resource.contentType());

        this.amazonS3.putObject(new PutObjectRequest(this.bucketName, name, new ByteArrayInputStream(resource.content()), metadata));
    }

    private byte[] getContent(final S3Object s3Object) {
        try (S3ObjectInputStream stream = s3Object.getObjectContent()) {
            return stream.readAllBytes();
        } catch (IOException e) {
            LOGGER.error("Error on read content of file in bucket {} message: {}", this.bucketName, e.getMessage());
            return new byte[]{};
        }
    }
}
