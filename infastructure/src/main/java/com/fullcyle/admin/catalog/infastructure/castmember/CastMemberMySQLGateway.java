package com.fullcyle.admin.catalog.infastructure.castmember;

import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcyle.admin.catalog.infastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcyle.admin.catalog.infastructure.castmember.persistence.CastMemberRepository;
import com.fullcyle.admin.catalog.infastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberMySQLGateway(CastMemberRepository castMemberRepository) {
        this.castMemberRepository = castMemberRepository;
    }


    @Override
    public CastMember create(final CastMember aCastMember) {
        return save(aCastMember);
    }

    @Override
    public void deleteById(final CastMemberID anID) {
        final var anIdValue = anID.getValue();

        if (this.castMemberRepository.existsById(anIdValue))
            this.castMemberRepository.deleteById(anIdValue);
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID anId) {
        return this.castMemberRepository.findById(anId.getValue()).map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember update(final CastMember aCastMember) {
        return save(aCastMember);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specification = Optional.ofNullable(aQuery.terms())
                .filter(term -> !term.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.castMemberRepository.findAll(Specification.where(specification), page);


        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CastMemberID> existsByIds(final Iterable<CastMemberID> ids) {
        throw new UnsupportedOperationException();
    }

    private CastMember save(CastMember aCastMember) {
        return this.castMemberRepository.save(CastMemberJpaEntity.from(aCastMember)).toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
