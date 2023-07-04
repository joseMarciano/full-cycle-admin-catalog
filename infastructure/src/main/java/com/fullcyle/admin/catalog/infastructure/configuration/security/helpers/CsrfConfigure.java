package com.fullcyle.admin.catalog.infastructure.configuration.security.helpers;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;

public class CsrfConfigure implements Customizer<CsrfConfigurer<HttpSecurity>> {
    @Override
    public void customize(final CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
        httpSecurityCsrfConfigurer.disable();
    }
}
