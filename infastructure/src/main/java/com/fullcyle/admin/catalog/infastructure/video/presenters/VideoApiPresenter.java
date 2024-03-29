package com.fullcyle.admin.catalog.infastructure.video.presenters;

import com.fullcyle.admin.catalog.application.video.media.upload.UploadMediaOuput;
import com.fullcyle.admin.catalog.application.video.retrieve.get.GetVideoByIdOutput;
import com.fullcyle.admin.catalog.application.video.retrieve.list.VideoListOutput;
import com.fullcyle.admin.catalog.application.video.update.UpdateVideoOutput;
import com.fullcyle.admin.catalog.domain.pagination.Pagination;
import com.fullcyle.admin.catalog.domain.video.AudioVideoMedia;
import com.fullcyle.admin.catalog.domain.video.ImageMedia;
import com.fullcyle.admin.catalog.infastructure.video.models.*;

import java.util.Objects;

public interface VideoApiPresenter {

    static VideoResponse present(final GetVideoByIdOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );


    }

    static AudioVideoMediaResponse present(final AudioVideoMedia video) {
        if (Objects.isNull(video)) return null;

        return new AudioVideoMediaResponse(
                video.id(),
                video.checksum(),
                video.name(),
                video.rawLocation(),
                video.encodedLocation(),
                video.status().name()
        );
    }

    static ImageMediaResponse present(final ImageMedia image) {
        if (Objects.isNull(image)) return null;

        return new ImageMediaResponse(
                image.id(),
                image.checksum(),
                image.name(),
                image.location()
        );
    }


    static UpdateVideoResponse present(final UpdateVideoOutput output) {
        return new UpdateVideoResponse(output.id());
    }

    static Pagination<VideoListResponse> present(final Pagination<VideoListOutput> page) {
        return page.map(VideoListResponse::from);
    }

    static UploadVideoResponse present(final UploadMediaOuput output) {
        return new UploadVideoResponse(output.videoId(), output.mediaType());
    }
}
