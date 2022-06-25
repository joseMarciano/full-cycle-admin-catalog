package com.fullcyle.admin.catalog.infastructure.category;

import com.fullcyle.admin.catalog.infastructure.MySQLGatewayTest;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void test() {

    }


}
