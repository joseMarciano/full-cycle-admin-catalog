package com.fullcyle.admin.catalog;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

class CleanUpExtension implements BeforeEachCallback {

    /*
        To execute callback and clear memory database;
        Could use @beforeEach in each test too;
    */
    @Override
    public void beforeEach(ExtensionContext context) {
        final var repositories = SpringExtension
                .getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values();

        cleanUp(repositories);
    }

    private void cleanUp(Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}