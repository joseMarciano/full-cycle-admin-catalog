package com.fullcyle.admin.catalog.application.castmember.create;

import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.Fixture;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(
                castMemberGateway
        );
    }


    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aCommnad = CreateCastMemberCommand.with(
                expectedName,
                expectedType
        );


        when(castMemberGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommnad);

        //then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(castMemberGateway).create(argThat(aMember ->
                Objects.nonNull(aMember.getId())
                        && Objects.equals(expectedName, aMember.getName())
                        && Objects.equals(expectedType, aMember.getType())
                        && Objects.nonNull(aMember.getCreatedAt())
                        && Objects.nonNull(aMember.getUpdatedAt())
        ));

    }

    @Test
    public void givenAInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        //given
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";


        final var aCommnad = CreateCastMemberCommand.with(
                expectedName,
                expectedType
        );

        //when
        final var actualException = assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommnad)
        );

        //then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidType_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        //given
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";


        final var aCommnad = CreateCastMemberCommand.with(
                expectedName,
                expectedType
        );

        //when
        final var actualException = assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommnad)
        );

        //then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).create(any());
    }
}
