package com.fullcyle.admin.catalog;

import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

class MySQLCleanUpExtension implements BeforeEachCallback {

    /*
        To execute callback and clear memory database;
        Could use @beforeEach in each test too;
    */
    @Override
    public void beforeEach(ExtensionContext context) {
        final var applicationContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                applicationContext.getBean(GenreRepository.class),
                applicationContext.getBean(CategoryRepository.class)
        ));
    }

    private void cleanUp(Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}