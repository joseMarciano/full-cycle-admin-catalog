package com.fullcyle.admin.catalog.infastructure.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties(prefix = "cloud-provider.aws.s3")
@Profile({"development", "production"})
public class S3Properties {

    private String bucketName;
    private String bucketRegion;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketRegion() {
        return bucketRegion;
    }

    public void setBucketRegion(final String bucketRegion) {
        this.bucketRegion = bucketRegion;
    }
}
