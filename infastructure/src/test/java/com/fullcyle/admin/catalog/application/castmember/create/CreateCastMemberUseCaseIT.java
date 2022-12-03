package com.fullcyle.admin.catalog.application.castmember.create;


import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.infastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.fullcyle.admin.catalog.domain.Fixture.CastMembers.type;
import static com.fullcyle.admin.catalog.domain.Fixture.name;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

    @Autowired
    private DefaultCreateCastMemberUseCase useCase;
    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnCastMemberId() {
        final var expectedName = name();
        final var expectedType = type();

        Assertions.assertEquals(0, castMemberRepository.count());

        final var aCommand =
                CreateCastMemberCommand.with(expectedName, expectedType);

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCastMember =
                castMemberRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCastMember_shouldReturnDomainException() {
        final String expectedName = null;
        final var expectedType = type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, castMemberRepository.count());


        final var aCommand =
                CreateCastMemberCommand.with(expectedName, expectedType);


        final var notification =
                Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
        Assertions.assertEquals(0, castMemberRepository.count());

        verify(castMemberGateway, times(0)).create(any());

    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = name();
        final var expectedType = type();
        final var expectedErrorMessage = "Gateway error";


        final var aCommand =
                CreateCastMemberCommand.with(expectedName, expectedType);

        //Using when is annotated with @SpyBean
        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(castMemberGateway).create(any());
//        when(castMemberGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        var actualOutput = Assertions
                .assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorMessage, actualOutput.getMessage());
    }
}
