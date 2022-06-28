package com.fullcyle.admin.catalog.infastructure.category;

import com.fullcyle.admin.catalog.domain.category.Category;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryID;
import com.fullcyle.admin.catalog.domain.category.CategorySearchQuery;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryJpaEntity;
import com.fullcyle.admin.catalog.infastructure.category.persistence.CategoryRepository;
import com.fullcyle.admin.catalog.infastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anID) {
        final var anIdValue = anID.getValue();

        if (this.repository.existsById(anIdValue))
            repository.deleteById(anIdValue);
    }

    @Override
    public Optional<Category> findById(CategoryID anID) {
        return repository.findById(anID.getValue())
                .map(CategoryJpaEntity::toAgregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specification = Optional.ofNullable(aQuery.terms())
                .filter(term -> !term.isBlank())
                .map(term -> SpecificationUtils
                        .<CategoryJpaEntity>like("name", term)
                        .or(SpecificationUtils.like("description", term)))
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specification), page);


        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAgregate).toList()
        );
    }

    private Category save(Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAgregate();
    }
}
