package com.fullcyle.admin.catalog.infastructure.configuration.security.helpers;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Oauth2ResourceServerConfigure implements Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String SEPARATOR = "_";
    private static final String ROLE = "ROLE_";

    @Override
    public void customize(final OAuth2ResourceServerConfigurer<HttpSecurity> httpSecurityOAuth2ResourceServerConfigurer) {
        httpSecurityOAuth2ResourceServerConfigurer.jwt()
                .jwtAuthenticationConverter(new KeycloakJwtConverter());
    }

    static class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private final KeycloakAuthoritiesConverter authoritiesConverter;

        public KeycloakJwtConverter() {
            this.authoritiesConverter = new KeycloakAuthoritiesConverter();
        }

        @Override
        public AbstractAuthenticationToken convert(final Jwt jwt) {
            return new JwtAuthenticationToken(jwt, extractAuthorities(jwt), extractPrincipal(jwt));
        }

        private String extractPrincipal(final Jwt jwt) {
            return jwt.getClaimAsString(JwtClaimNames.SUB);
        }

        private Collection<? extends GrantedAuthority> extractAuthorities(final Jwt jwt) {
            return this.authoritiesConverter.convert(jwt);
        }
    }

    static class KeycloakAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(final Jwt jwt) {
            final var realmRoles = extractRealmRoles(jwt);
            final var resourceRoles = extractResourceRoles(jwt);

            return Stream.concat(realmRoles, resourceRoles)
                    .map(String::toUpperCase)
                    .map(str -> ROLE + str)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }

        private Stream<String> extractResourceRoles(final Jwt jwt) {
            final Function<Map.Entry<String, Object>, Stream<String>> mapResource = resource -> {
                final var key = resource.getKey();
                final var value = (JSONObject) resource.getValue();
                final var roles = (Collection<String>) value.get(ROLES);
                return roles.stream().map(role -> key.concat(SEPARATOR).concat(role));
            };

            final Function<Set<Map.Entry<String, Object>>, Collection<String>> mapResources = resources ->
                    resources.stream()
                            .flatMap(mapResource)
                            .toList();

            return Optional.ofNullable(jwt.getClaimAsMap(RESOURCE_ACCESS))
                    .map(Map::entrySet)
                    .map(mapResources)
                    .orElse(Collections.emptyList())
                    .stream();
        }

        private Stream<String> extractRealmRoles(final Jwt jwt) {
            return Optional.ofNullable(jwt.getClaimAsMap(REALM_ACCESS))
                    .map(resource -> (Collection<String>) resource.get(ROLES))
                    .orElse(Collections.emptyList())
                    .stream();
        }
    }
}
