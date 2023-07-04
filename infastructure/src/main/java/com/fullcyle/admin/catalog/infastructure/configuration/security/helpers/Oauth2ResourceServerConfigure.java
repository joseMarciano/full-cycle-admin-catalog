package com.fullcyle.admin.catalog.infastructure.configuration.security.helpers;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

public class Oauth2ResourceServerConfigure implements Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> {
    @Override
    public void customize(final OAuth2ResourceServerConfigurer<HttpSecurity> httpSecurityOAuth2ResourceServerConfigurer) {
        httpSecurityOAuth2ResourceServerConfigurer.jwt(); // TODO: CONFIGURE JWT CONVERTER FOR KEYCLOAK
    }
}
