package com.fullcyle.admin.catalog;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

import static org.springframework.context.annotation.FilterType.REGEX;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test") // to configure H2 instead MySql
@DataJpaTest
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = REGEX, pattern = ".*[MySQLGateway]")
})
@ExtendWith(CleanUpExtension.class)
public @interface MySQLGatewayTest {
}
