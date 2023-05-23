package com.fullcyle.admin.catalog.infastructure.api.controllers;

import com.fullcyle.admin.catalog.application.video.create.CreateVideoCommand;
import com.fullcyle.admin.catalog.application.video.create.CreateVideoUseCase;
import com.fullcyle.admin.catalog.domain.video.Resource;
import com.fullcyle.admin.catalog.infastructure.api.VideoAPI;
import com.fullcyle.admin.catalog.infastructure.utils.HashingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.Year;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;

    public VideoController(final CreateVideoUseCase createVideoUseCase) {
        this.createVideoUseCase = createVideoUseCase;
    }


    @Override
    public ResponseEntity<?> createFull(final String title,
                                        final String description,
                                        final Integer yearLaunched,
                                        final Double duration,
                                        final Boolean opened,
                                        final Boolean published,
                                        final String rating,
                                        final Set<String> categoriesId,
                                        final Set<String> genresId,
                                        final Set<String> castMembersId,
                                        final MultipartFile videoFile,
                                        final MultipartFile trailerFile,
                                        final MultipartFile bannerFile,
                                        final MultipartFile thumbFile,
                                        final MultipartFile thumbHalfFile) {

        final var aCommand = CreateVideoCommand.with(
                title,
                description,
                Year.of(yearLaunched),
                duration,
                opened,
                published,
                rating,
                categoriesId,
                genresId,
                castMembersId,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile)
        );


        final var output = this.createVideoUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    private Resource resourceOf(final MultipartFile part) {
        if (Objects.isNull(part)) return null;

        try {
            final byte[] content = part.getBytes();
            return Resource.with(
                    HashingUtils.checksum(content),
                    content,
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
