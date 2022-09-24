package com.fullcyle.admin.catalog.infastructure.castmember;


import com.fullcyle.admin.catalog.MySQLGatewayTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcyle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcyle.admin.catalog.infastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcyle.admin.catalog.infastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.fullcyle.admin.catalog.Fixture.CastMember.type;
import static com.fullcyle.admin.catalog.Fixture.name;
import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        final var expectedName = name();
        final var expectedType = type();

        final var aCastMember = CastMember.newMember(expectedName, expectedType);

        assertEquals(0, castMemberRepository.count());
        final var actualCastMember = castMemberGateway.create(aCastMember);
        assertEquals(1, castMemberRepository.count());

        assertEquals(expectedName, aCastMember.getName());
        assertEquals(expectedType, aCastMember.getType());
        assertEquals(aCastMember.getId(), actualCastMember.getId());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        assertEquals(aCastMember.getUpdatedAt(), actualCastMember.getUpdatedAt());

        final var actualEntity = castMemberRepository.findById(aCastMember.getId().getValue()).get();

        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedType, actualEntity.getType());
        assertEquals(aCastMember.getId().getValue(), actualEntity.getId());
        assertEquals(aCastMember.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCastMember.getUpdatedAt(), actualEntity.getUpdatedAt());

    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldReturnACastMemberUpdated() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var aCastMember = CastMember.newMember("vin dizu", CastMemberType.DIRECTOR);

        assertEquals(0, castMemberRepository.count());
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));
        assertEquals(1, castMemberRepository.count());

        final var updatedCastMember = CastMember.with(aCastMember).update(expectedName, expectedType);

        final var actualCastMember = castMemberGateway.update(updatedCastMember);
        assertEquals(1, castMemberRepository.count());

        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertEquals(aCastMember.getId(), actualCastMember.getId());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        assertTrue(actualCastMember.getCreatedAt().isBefore(actualCastMember.getUpdatedAt()));

        final var actualEntity = castMemberRepository.findById(aCastMember.getId().getValue()).get();

        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedType, actualEntity.getType());
        assertEquals(aCastMember.getId().getValue(), actualEntity.getId());
        assertEquals(aCastMember.getCreatedAt(), actualEntity.getCreatedAt());
        assertTrue(aCastMember.getCreatedAt().isBefore(actualEntity.getUpdatedAt()));

    }

    @Test
    public void givenAPrePersistedCastMemberAndValidCastMemberId_whenTryToDeleteIt_shouldDeleteCastMember() {
        final var aCastMember = CastMember.newMember(name(), type());

        assertEquals(0, castMemberRepository.count());
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));
        assertEquals(1, castMemberRepository.count());

        castMemberGateway.deleteById(aCastMember.getId());
        assertEquals(0, castMemberRepository.count());

    }

    @Test
    public void givenAInvalidCastMemberId_whenTryToDeleteIt_shouldDeleteCastMember() {
        assertEquals(0, castMemberRepository.count());
        castMemberGateway.deleteById(CastMemberID.from("invalid"));
        assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenAPrePersistedCastMemberAndValidCastMemberId_whenCallsFindById_shouldReturnACastMember() {
        final var expectedName = name();
        final var expectedType = type();

        final var aCastMember = CastMember.newMember(expectedName, expectedType);
        assertEquals(0, castMemberRepository.count());
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aCastMember));
        assertEquals(1, castMemberRepository.count());

        final var actualCastMember = castMemberGateway.findById(aCastMember.getId()).get();
        assertEquals(1, castMemberRepository.count());

        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertEquals(aCastMember.getId(), actualCastMember.getId());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        assertEquals(aCastMember.getUpdatedAt(), actualCastMember.getUpdatedAt());

    }

    @Test
    public void givenAValidCastMemberIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, castMemberRepository.count());
        final var optionalCastMember = castMemberGateway.findById(CastMemberID.from("validNotStoredID"));
        assertFalse(optionalCastMember.isPresent());
    }


    @Test
    public void givenEmptyCastMember_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = castMemberGateway.findAll(aQuery);

        //then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.items().size());
        assertEquals(expectedTotal, actualPage.total());

    }


    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "nks,0,10,1,1,Tom Hanks",
            "wi,0,10,1,1,Will Smith",
            "shin,0,10,1,1,Denzel Washington",
            "Jack,0,10,1,1,Samuel L. Jackson",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final long expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {

        // given
        mockCastMember();
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = castMemberGateway.findAll(aQuery);

        //then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Denzel Washington",
            "name,desc,0,10,5,5,Will Smith",
            "createdAt,asc,0,10,5,5,Vin Diesel",
            "createdAt,desc,0,10,5,5,Samuel L. Jackson"
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final long expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {

        // given
        mockCastMember();
        final var expectedTerms = "";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = castMemberGateway.findAll(aQuery);

        //then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());
        assertEquals(expectedGenreName, actualPage.items().get(0).getName());

    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Denzel Washington;Samuel L. Jackson",
            "1,2,2,5,Tom Hanks;Vin Diesel",
            "2,2,1,5,Will Smith",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final long expectedItemsCount,
            final long expectedTotal,
            final String expectedCastMember
    ) {

        // given
        mockCastMember();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = castMemberGateway.findAll(aQuery);

        //then
        assertEquals(expectedPage, actualPage.currentPage());
        assertEquals(expectedPerPage, actualPage.perPage());
        assertEquals(expectedTotal, actualPage.total());
        assertEquals(expectedItemsCount, actualPage.items().size());

        final var genreNames = List.of(expectedCastMember.split(";"));
        assertTrue(actualPage.items().stream().map(CastMember::getName).toList().containsAll(genreNames));

    }

    private void mockCastMember() {
        castMemberRepository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", type())),
                CastMemberJpaEntity.from(CastMember.newMember("Denzel Washington", type())),
                CastMemberJpaEntity.from(CastMember.newMember("Tom Hanks", type())),
                CastMemberJpaEntity.from(CastMember.newMember("Will Smith", type())),
                CastMemberJpaEntity.from(CastMember.newMember("Samuel L. Jackson", type())
                )));
    }

}