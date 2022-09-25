package com.fullcyle.admin.catalog.application.castmember.delete;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.infastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.fullcyle.admin.catalog.Fixture.CastMember.type;
import static com.fullcyle.admin.catalog.Fixture.name;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {

    @Autowired
    private DefaultDeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenAValidCastMemberId_whenCallsDeleteCastMember_shouldDeleteCastMember() {
        //given
        final var aCastMember = castMemberGateway.create(CastMember.newMember(name(), type()));
        final var expectedId = aCastMember.getId();

        //when
        assertEquals(1, castMemberRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));


        // then
        assertEquals(0, castMemberRepository.count());

    }

    @Test
    public void givenAnInvalidCastMemberId_whenCallsDeleteCastMember_shouldBeOk() {
        //given
        castMemberGateway.create(CastMember.newMember(name(), type()));
        final var expectedId = CastMemberID.from("123");

        //when
        assertEquals(1, castMemberRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        assertEquals(1, castMemberRepository.count());
    }
}
