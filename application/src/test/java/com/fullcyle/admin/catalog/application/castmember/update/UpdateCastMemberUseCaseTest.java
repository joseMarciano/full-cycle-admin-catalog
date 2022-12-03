package com.fullcyle.admin.catalog.application.castmember.update;

import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.Fixture;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcyle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(
                castMemberGateway
        );
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnsItsIdentifier() {
        //given
        final var aCastMember =
                CastMember.newMember("vin disu", CastMemberType.DIRECTOR);

        final var expectedId = aCastMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommamd =
                UpdateCastMemberCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedType
                );

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(CastMember.with(aCastMember)));

        when(castMemberGateway.update(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOuput = useCase.execute(aCommamd);

        //then
        Assertions.assertNotNull(actualOuput);
        Assertions.assertNotNull(actualOuput.id(), aCastMember.getId().getValue());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway).update(argThat(aUpdatedMember ->
                Objects.equals(expectedId, aUpdatedMember.getId())
                        && Objects.equals(expectedName, aUpdatedMember.getName())
                        && Objects.equals(expectedType, aUpdatedMember.getType())
                        && Objects.equals(aCastMember.getCreatedAt(), aUpdatedMember.getCreatedAt())
                        && aCastMember.getUpdatedAt().isBefore(aUpdatedMember.getUpdatedAt())
        ));
    }


    @Test
    public void givenAInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        //given
        final var aCastMember =
                CastMember.newMember("vin disu", CastMemberType.DIRECTOR);

        final var expectedId = aCastMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommamd =
                UpdateCastMemberCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedType
                );

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(aCastMember));

        //when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommamd));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        //given
        final var aCastMember =
                CastMember.newMember("vin disu", CastMemberType.DIRECTOR);

        final var expectedId = aCastMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommamd =
                UpdateCastMemberCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedType
                );

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(aCastMember));

        //when
        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommamd));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());


        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidID_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        //given
        final var aCastMember =
                CastMember.newMember("vin disu", CastMemberType.DIRECTOR);

        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommamd =
                UpdateCastMemberCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedType
                );

        when(castMemberGateway.findById(any())).thenReturn(Optional.empty());

        //when
        final var actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommamd));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());


        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }
}
