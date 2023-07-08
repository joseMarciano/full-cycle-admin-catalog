package com.fullcyle.admin.catalog.infastructure.configuration.security.helpers;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

public class AuthorizeRequestConfigure implements Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> {

    private static final String ROLE_ADMIN = "CATALOGO_ADMIN";
    private static final String CATALOGO_CAST_MEMBERS = "CATALOGO_CAST_MEMBERS";
    private static final String CATALOGO_CATEGORIES = "CATALOGO_CATEGORIES";
    private static final String CATALOGO_GENRES = "CATALOGO_GENRES";
    private static final String CATALOGO_VIDEOS = "CATALOGO_VIDEOS";

    @Override
    public void customize(final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorize) {
        authorize
                .antMatchers("/cast_members*", "/cast_members/*").hasAnyRole(ROLE_ADMIN, CATALOGO_CAST_MEMBERS)
                .antMatchers("/categories*", "/categories/*").hasAnyRole(ROLE_ADMIN, CATALOGO_CATEGORIES)
                .antMatchers("/genres*", "/genres/*").hasAnyRole(ROLE_ADMIN, CATALOGO_GENRES)
                .antMatchers("/videos*", "/videos/*").hasAnyRole(ROLE_ADMIN, CATALOGO_VIDEOS)
                .anyRequest()
                .hasRole(ROLE_ADMIN);
    }
}
