package com.fullcyle.admin.catalog.domain.castmember;


import com.fullcyle.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest {

    @Test
    public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidNameWithLenghtMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = """
                A nível organizacional, o início da atividade geral de formação de atitudes acarreta um processo de ref
                ormulação e modernização das condições inegavelmente apropriadas. Não obstante, o desafiador cenário glo
                balizado agrega valor ao estabelecimento do processo de comunicação como um todo. Percebemos, cada vez
                mais, que a consolidação das estruturas representa uma abertura para a melhoria das diretrizes de
                desenvolvimento para o futuro.
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException =
                Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
