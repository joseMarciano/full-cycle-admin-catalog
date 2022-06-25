package com.fullcyle.admin.catalog.infastructure;


import com.fullcyle.admin.catalog.infastructure.category.CategoryMySQLGatewayTest;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

import static org.springframework.context.annotation.FilterType.REGEX;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test") // to configure H2 instead MySql
@DataJpaTest
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = REGEX, pattern = ".*[MySQLGateway]")
})
@ExtendWith(MySQLGatewayTest.CleanUpExtensions.class)
public @interface MySQLGatewayTest {

    /*
      To execute callback and clear memory database;
      Could use @beforeEach in each test too;
  */
    class CleanUpExtensions implements BeforeEachCallback {

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
}
