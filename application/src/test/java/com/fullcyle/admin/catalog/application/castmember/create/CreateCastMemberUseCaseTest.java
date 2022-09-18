package com.fullcyle.admin.catalog.application.castmember.create;

import com.fullcyle.admin.catalog.application.Fixture;
import com.fullcyle.admin.catalog.application.UseCaseTest;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

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
        final var expectedType = Fixture.CastMember.type();

        final var aCommnad = CreateCastMemberCommand.with(
                expectedName,
                expectedType
        );


        when(castMemberGateway.create(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommnad);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(castMemberGateway).create(argThat(aMember ->
                Objects.nonNull(aMember.getId())
                        && Objects.equals(expectedName, aMember.getName())
                        && Objects.equals(expectedType, aMember.getType())
                        && Objects.nonNull(aMember.getCreatedAt())
                        && Objects.nonNull(aMember.getUpdatedAt())
        ));

    }
}
