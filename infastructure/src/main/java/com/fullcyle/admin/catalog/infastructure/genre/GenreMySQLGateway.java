package com.fullcyle.admin.catalog.infastructure.genre;

import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreJpaEntity;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }


    @Override
    public Genre create(Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(GenreID anID) {

    }

    @Override
    public Optional<Genre> findById(GenreID anId) {
        return Optional.empty();
    }

    @Override
    public Genre update(Genre aGenre) {
        return null;
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        return null;
    }

    private Genre save(Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre))
                .toAggreate();
    }
}
