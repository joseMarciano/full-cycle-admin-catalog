package com.fullcyle.admin.catalog.infastructure.configuration.usecases;

import com.fullcyle.admin.catalog.application.video.create.CreateVideoUseCase;
import com.fullcyle.admin.catalog.application.video.create.DefaultCreateVideoUseCase;
import com.fullcyle.admin.catalog.application.video.delete.DefaultDeleteVideoUseCase;
import com.fullcyle.admin.catalog.application.video.delete.DeleteVideoUseCase;
import com.fullcyle.admin.catalog.application.video.media.get.DefaultGetMediaUseCase;
import com.fullcyle.admin.catalog.application.video.media.get.GetMediaUseCase;
import com.fullcyle.admin.catalog.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.fullcyle.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcyle.admin.catalog.application.video.media.upload.DefaultUploadMediaUseCase;
import com.fullcyle.admin.catalog.application.video.media.upload.UploadMediaUseCase;
import com.fullcyle.admin.catalog.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.fullcyle.admin.catalog.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcyle.admin.catalog.application.video.retrieve.list.DefaultListVideosUseCase;
import com.fullcyle.admin.catalog.application.video.retrieve.list.ListVideoUseCase;
import com.fullcyle.admin.catalog.application.video.update.DefaultUpdateVideoUseCase;
import com.fullcyle.admin.catalog.application.video.update.UpdateVideoUseCase;
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

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(this.videoGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(categoryGateway, genreGateway, castMemberGateway, videoGateway, mediaResourceGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideoUseCase listVideoUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(videoGateway, mediaResourceGateway);
    }

}
