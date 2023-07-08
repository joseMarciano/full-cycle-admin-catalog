package com.fullcyle.admin.catalog;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

public interface ApiTest {

    JwtRequestPostProcessor ADMIN_JWT =
            SecurityMockMvcRequestPostProcessors.jwt()
                    .authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_ADMIN"));

    JwtRequestPostProcessor CATEGORIES_JWT =
            SecurityMockMvcRequestPostProcessors.jwt()
                    .authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_CATEGORIES"));

    JwtRequestPostProcessor CAST_MEMBERS_JWT =
            SecurityMockMvcRequestPostProcessors.jwt()
                    .authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_CAST_MEMBERS"));

    JwtRequestPostProcessor GENRES_JWT =
            SecurityMockMvcRequestPostProcessors.jwt()
                    .authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_GENRES"));

    JwtRequestPostProcessor VIDEOS_JWT =
            SecurityMockMvcRequestPostProcessors.jwt()
                    .authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_VIDEOS"));


}
