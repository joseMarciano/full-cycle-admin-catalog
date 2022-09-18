package com.fullcyle.admin.catalog.application.castmember.retrieve.get;

import com.fullcyle.admin.catalog.application.Fixture;
import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberByIdUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(
                castMemberGateway
        );
    }

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnCastMember() {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aCastMember =
                CastMember.newMember(expectedName, expectedType);

        final var expectedId = aCastMember.getId();

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aCastMember));

        //when
        final var actualCastMember = useCase.execute(expectedId.getValue());

        //then
        assertEquals(expectedId.getValue(), actualCastMember.id());
        assertEquals(expectedName, actualCastMember.name());
        assertEquals(expectedType, actualCastMember.type());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.createdAt());
        assertEquals(aCastMember.getUpdatedAt(), actualCastMember.updatedAt());

        verify(castMemberGateway, times(1)).findById(expectedId);
    }


    @Test
    public void givenAValidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFound() {
        //given
        final var expctedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        when(castMemberGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () ->
                useCase.execute(expectedId.getValue())
        );

        //then
        assertEquals(expctedErrorMessage, actualException.getMessage());
        verify(castMemberGateway, times(1)).findById(expectedId);
    }
}
