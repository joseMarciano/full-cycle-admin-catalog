package com.fullcyle.admin.catalog.infastructure.amqp;

import com.fullcyle.admin.catalog.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcyle.admin.catalog.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcyle.admin.catalog.domain.video.MediaStatus;
import com.fullcyle.admin.catalog.infastructure.configuration.json.Json;
import com.fullcyle.admin.catalog.infastructure.video.models.VideoEncoderCompleted;
import com.fullcyle.admin.catalog.infastructure.video.models.VideoEncoderError;
import com.fullcyle.admin.catalog.infastructure.video.models.VideoEncoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class VideoEncoderListener {

    private static final Logger log = LoggerFactory.getLogger(VideoEncoderListener.class);
    public static final String LISTENER_ID = "videoEncodedListener";
    private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

    public VideoEncoderListener(final UpdateMediaStatusUseCase updateMediaStatusUseCase) {
        this.updateMediaStatusUseCase = updateMediaStatusUseCase;
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncodedMessage(@Payload final String message) {
        final var result = Json.readValue(message, VideoEncoderResult.class);

        if (result instanceof VideoEncoderCompleted dto) {
            resolveCompleted(dto);
            log.info("[message:video.listener.income] [status:completed] [payload:{}]", message);
            return;
        }

        if (result instanceof VideoEncoderError dto) {
            //TODO: NEED TO BE IMPLEMENTED
            log.error("[message:video.listener.income] [status:error] [payload:{}]", message);
            return;
        }

        log.error("[message:video.listener.income] [status:unknown] [payload:{}]", message);
    }

    private void resolveCompleted(final VideoEncoderCompleted dto) {
        final var aCmd = new UpdateMediaStatusCommand(
                MediaStatus.COMPLETED,
                dto.id(),
                dto.video().resourceId(),
                dto.video().encodedVideoFolder(),
                dto.video().filePath()
        );

        this.updateMediaStatusUseCase.execute(aCmd);
    }
}
