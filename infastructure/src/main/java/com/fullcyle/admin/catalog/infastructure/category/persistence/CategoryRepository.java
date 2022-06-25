package com.fullcyle.admin.catalog.infastructure.category.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {
}
