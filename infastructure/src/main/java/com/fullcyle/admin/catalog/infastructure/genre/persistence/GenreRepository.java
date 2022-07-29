package com.fullcyle.admin.catalog.infastructure.genre.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreJpaEntity, String> {}
