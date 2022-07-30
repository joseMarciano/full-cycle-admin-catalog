package com.fullcyle.admin.catalog.infastructure.genre;

import com.fullcyle.admin.catalog.domain.genre.Genre;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreID;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreJpaEntity;
import com.fullcyle.admin.catalog.infastructure.genre.persistence.GenreRepository;
import com.fullcyle.admin.catalog.infastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }


    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(final GenreID anID) {
        final var aGenreId = anID.getValue();

        if (isNull(aGenreId) || aGenreId.isBlank() || !this.genreRepository.existsById(aGenreId))
            return;

        this.genreRepository.deleteById(aGenreId);
    }

    @Override
    public Optional<Genre> findById(GenreID anId) {
        return this.genreRepository.findById(anId.getValue())
                .map(GenreJpaEntity::toAggreate);
    }

    @Override
    public Genre update(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specification = Optional.ofNullable(aQuery.terms())
                .filter(term -> !term.isBlank())
                .map(this::asembleSpecification)
                .orElse(null);

        final var pageResult = this.genreRepository.findAll(Specification.where(specification), page);


        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(GenreJpaEntity::toAggreate).toList()
        );
    }

    private Specification<GenreJpaEntity> asembleSpecification(final String terms) {
        return SpecificationUtils.<GenreJpaEntity>like("name", terms);
    }

    private Genre save(Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre))
                .toAggreate();
    }
}
