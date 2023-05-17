package com.fullcyle.admin.catalog.infastructure.configuration.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.fullcyle.admin.catalog.infastructure.configuration.properties.AwsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"development", "production"})
public class AwsCredentialsConfig {

    private final AwsProperties awsProperties;

    public AwsCredentialsConfig(final AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

    @Bean
    public AWSCredentials getCredentials() {
        return new BasicAWSCredentials(
                this.awsProperties.getAccessKey(),
                this.awsProperties.getSecretKey()
        );
    }
}
