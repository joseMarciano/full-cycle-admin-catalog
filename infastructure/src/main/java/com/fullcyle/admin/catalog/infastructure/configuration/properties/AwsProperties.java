package com.fullcyle.admin.catalog.infastructure.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties(prefix = "cloud-provider.aws")
@Profile({"development", "production"})
public class AwsProperties {

    private String accessKey;
    private String secretKey;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }
}
