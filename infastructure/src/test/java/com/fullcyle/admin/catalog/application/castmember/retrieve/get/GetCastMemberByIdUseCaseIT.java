package com.fullcyle.admin.catalog.application.castmember.retrieve.get;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.fullcyle.admin.catalog.domain.Fixture.CastMembers.type;
import static com.fullcyle.admin.catalog.domain.Fixture.name;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class GetCastMemberByIdUseCaseIT {

    @Autowired
    private DefaultGetCastMemberByIdUseCase useCase;

    @Autowired
    private CastMemberGateway castMemberGateway;


    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnCastMember() {
        //given
        final var expectedName = name();
        final var expectedType = type();

        final var aCastMember =
                CastMember.newMember(expectedName, expectedType);

        final var expectedId = aCastMember.getId();

        castMemberGateway.create(aCastMember);

        //when
        final var actualCastMember = useCase.execute(expectedId.getValue());

        //then
        assertEquals(expectedId.getValue(), actualCastMember.id());
        assertEquals(expectedName, actualCastMember.name());
        assertEquals(expectedType, actualCastMember.type());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.createdAt());
        assertEquals(aCastMember.getUpdatedAt(), actualCastMember.updatedAt());
    }


    @Test
    public void givenAValidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFound() {
        //given
        final var expctedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () ->
                useCase.execute(expectedId.getValue())
        );

        //then
        assertEquals(expctedErrorMessage, actualException.getMessage());
    }
}
