package com.fullcyle.admin.catalog.e2e.category;

import com.fullcyle.admin.catalog.E2ETest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER =
            new MySQLContainer("mysql:latest") //put the prod version
                    .withPassword("123456")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");


    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) { // to customize spring propeties in runtime
        final var mapperPort = MY_SQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s\n", mapperPort);
        registry.add("mysql.port", () -> mapperPort);
    }

    @Test
    public void testWorks() {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
    }

}
