package com.fullcyle.admin.catalog.infastructure.configuration.security;

import com.fullcyle.admin.catalog.infastructure.configuration.security.helpers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(new CsrfConfigure())
                .authorizeRequests(new AuthorizeRequestConfigure())
                .oauth2ResourceServer(new Oauth2ResourceServerConfigure())
                .sessionManagement(new SessionManagementConfigure())
                .headers(new HeadersConfigure())
                .build();
    }
}
