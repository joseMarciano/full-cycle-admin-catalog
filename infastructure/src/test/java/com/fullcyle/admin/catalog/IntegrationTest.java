package com.fullcyle.admin.catalog;


import com.fullcyle.admin.catalog.infastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

import static org.springframework.context.annotation.FilterType.REGEX;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(CleanUpExtension.class)
@ActiveProfiles("test") // to configure H2 instead MySql
public @interface IntegrationTest {
}
