package com.fullcyle.admin.catalog.infastructure.configuration.security.helpers;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

public class HeadersConfigure implements Customizer<HeadersConfigurer<HttpSecurity>> {
    @Override
    public void customize(final HeadersConfigurer<HttpSecurity> headers) {
        headers.frameOptions().sameOrigin();
    }
}
