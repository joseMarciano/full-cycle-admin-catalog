package com.fullcyle.admin.catalog.infastructure.configuration.usecases;

import com.fullcyle.admin.catalog.application.video.create.CreateVideoUseCase;
import com.fullcyle.admin.catalog.application.video.create.DefaultCreateVideoUseCase;
import com.fullcyle.admin.catalog.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.fullcyle.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcyle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcyle.admin.catalog.domain.category.CategoryGateway;
import com.fullcyle.admin.catalog.domain.genre.GenreGateway;
import com.fullcyle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcyle.admin.catalog.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoUseCaseConfig {

    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public VideoUseCaseConfig(final VideoGateway videoGateway,
                              final CategoryGateway categoryGateway,
                              final GenreGateway genreGateway,
                              final CastMemberGateway castMemberGateway,
                              final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = videoGateway;
        this.categoryGateway = categoryGateway;
        this.genreGateway = genreGateway;
        this.castMemberGateway = castMemberGateway;
        this.mediaResourceGateway = mediaResourceGateway;
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(
                this.categoryGateway,
                this.genreGateway,
                this.castMemberGateway,
                this.videoGateway,
                this.mediaResourceGateway
        );
    }
}
