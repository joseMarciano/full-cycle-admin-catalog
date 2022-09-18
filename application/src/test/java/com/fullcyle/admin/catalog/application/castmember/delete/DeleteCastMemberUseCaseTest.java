package com.fullcyle.admin.catalog.application.castmember.delete;

import com.fullcyle.admin.catalog.application.Fixture;
import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;


public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldBeOK() {
        final var aCastMember = CastMember.newMember(
                Fixture.name(),
                Fixture.CastMember.type()
        );

        final var expectedId = aCastMember.getId();

        Mockito.doNothing()
                .when(castMemberGateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(castMemberGateway, times(1)).deleteById(eq(expectedId));

    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCastMember_shouldBeOK() {
        final var expectedId = CastMemberID.from("123");

        Mockito.doNothing()
                .when(castMemberGateway).deleteById(eq(expectedId));


        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Mockito.verify(castMemberGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnException() {
        final var aCastMember = CastMember.newMember(
                Fixture.name(),
                Fixture.CastMember.type()
        );

        final var expectedId = aCastMember.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(eq(expectedId));

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        Mockito.verify(castMemberGateway, times(1)).deleteById(eq(expectedId));

    }

}
