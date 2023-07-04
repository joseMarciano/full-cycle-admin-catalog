package com.fullcyle.admin.catalog.infastructure.configuration.security.helpers;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

public class SessionManagementConfigure implements Customizer<SessionManagementConfigurer<HttpSecurity>> {
    @Override
    public void customize(final SessionManagementConfigurer<HttpSecurity> sessionManagement) {
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
