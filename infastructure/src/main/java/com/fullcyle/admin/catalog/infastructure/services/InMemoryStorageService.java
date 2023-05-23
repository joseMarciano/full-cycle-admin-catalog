package com.fullcyle.admin.catalog.infastructure.services;

import com.fullcyle.admin.catalog.domain.video.Resource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Profile({"test-e2e", "test-integration"})
public class InMemoryStorageService implements StorageService { // THIS CLASS WAS CREATED TO SIMULATES S3 TESTS

    @Override
    public void deleteAll(final Collection<String> names) {
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.empty();
    }
//8ed354f3b7094655ac012ccbc8cb83fb category
//    fbaf2273c11543e1853e92ad0394d7e2 genre
//            67e2afea82b44b74bf077e87c7214d22 cast_member

    @Override
    public List<String> list(final String prefix) {
        return Collections.emptyList();
    }

    @Override
    public void store(final String name, final Resource resource) {
    }

}
