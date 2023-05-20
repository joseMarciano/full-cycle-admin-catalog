package com.fullcyle.admin.catalog.infastructure.services;

import com.fullcyle.admin.catalog.domain.video.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class InMemoryStorageService implements StorageService { // THIS CLASS WAS CREATED TO SIMULATES S3 TESTS

    @Override
    public void deleteAll(final Collection<String> names) {
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.empty();
    }


    @Override
    public List<String> list(final String prefix) {
        return Collections.emptyList();
    }

    @Override
    public void store(final String name, final Resource resource) {
    }

}
