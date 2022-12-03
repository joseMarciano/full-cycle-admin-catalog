package com.fullcyle.admin.catalog.application.castmember.update;

import com.fullcyle.admin.catalog.IntegrationTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMember;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberType;
import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import com.fullcyle.admin.catalog.infastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.fullcyle.admin.catalog.domain.Fixture.name;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private DefaultUpdateCastMemberUseCase useCase;
    @SpyBean
    private CastMemberGateway castMemberGateway;
    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnCastMemberId() {
        // given
        final var aCastMember =
                castMemberGateway.create(CastMember.newMember(name(), CastMemberType.ACTOR));

        final var expectedId = aCastMember.getId();
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.DIRECTOR;

        final var acommand =
                UpdateCastMemberCommand.with(
                        expectedId.getValue(),
                        expectedName,
                        expectedType
                );

        // when
        final var actualOutput = useCase.execute(acommand);

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualCastMember = castMemberRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualCastMember.getName());
        assertEquals(expectedType, actualCastMember.getType());
        assertEquals(aCastMember.getCreatedAt(), actualCastMember.getCreatedAt());
        assertTrue(aCastMember.getUpdatedAt().isBefore(actualCastMember.getUpdatedAt()));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCastMember_shouldReturnNotificationException() {
        // given
        final var aCastMember =
                castMemberGateway.create(CastMember.newMember(name(), CastMemberType.DIRECTOR));

        final var expectedId = aCastMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.DIRECTOR;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var acommand =
                UpdateCastMemberCommand.with(expectedId.getValue(),
                        expectedName,
                        expectedType
                );

        // when
        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(acommand));

        // then

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway, times(1)).findById(expectedId);
        verify(castMemberGateway, times(0)).update(any());
    }
}
