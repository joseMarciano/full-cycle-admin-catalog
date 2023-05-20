package com.fullcyle.admin.catalog.infastructure.services;

import com.fullcyle.admin.catalog.AmqpTest;
import com.fullcyle.admin.catalog.domain.utils.IdUtils;
import com.fullcyle.admin.catalog.infastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@AmqpTest
public class RabbitEventServiceTest {

    private static final String LISTENER = "video.created";
    @Autowired
    @Qualifier("videoCreatedEventService")
    private EventsService publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void shouldSendMessage() throws InterruptedException {
        //given
        final var notification = new HashMap<>();
        notification.put("id", IdUtils.uuid());

        final var expectedMessage = Json.writeValueAsString(notification);

        //when
        this.publisher.send(notification);


        //then
        final var invocationData = harness.getNextInvocationDataFor(LISTENER, 2, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        Assertions.assertEquals(expectedMessage, actualMessage);

    }

    @Component
    static class VideoCreatedNewsListener {
        @RabbitListener(id = LISTENER, queues = "${amqp.queues.video-created.routing-key}")
        void onVideoCreated(@Payload String message) {

        }

    }
}
