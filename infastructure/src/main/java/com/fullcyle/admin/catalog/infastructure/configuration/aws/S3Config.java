package com.fullcyle.admin.catalog.infastructure.configuration.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fullcyle.admin.catalog.infastructure.configuration.properties.S3Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    private final S3Properties s3Properties;


    public S3Config(final S3Properties s3Properties) {
        this.s3Properties = s3Properties;
    }

    @Bean
    public AmazonS3 s3Client(final AWSCredentials awsCredentials) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.fromName(this.s3Properties.getBucketRegion()))
                .build();
    }
}
