package com.fullcyle.admin.catalog;


import com.fullcyle.admin.catalog.infastructure.configuration.WebServerConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@SpringBootTest(classes = WebServerConfig.class)
@ActiveProfiles("test-integration")
public @interface AmqpTest {
}
