package com.fullcyle.admin.catalog.infastructure.configuration.usecases;


import com.fullcyle.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.fullcyle.admin.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.fullcyle.admin.catalog.application.castmember.retrieve.list.DefaultListCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.fullcyle.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMemberUseCase listCastMembersUseCase() {
        return new DefaultListCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }
}
