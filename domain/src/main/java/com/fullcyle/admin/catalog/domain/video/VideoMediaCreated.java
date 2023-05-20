package com.fullcyle.admin.catalog.domain.video;

import com.fullcyle.admin.catalog.domain.events.DomainEvent;
import com.fullcyle.admin.catalog.domain.utils.InstantUtils;

import java.time.Instant;

public class VideoMediaCreated implements DomainEvent {
    private final String videoId;
    private final String rawLocation;
    private final Instant occuredOn;

    public VideoMediaCreated(final String videoId, final String rawLocation) {
        this.videoId = videoId;
        this.rawLocation = rawLocation;
        this.occuredOn = InstantUtils.now();
    }

    @Override
    public Instant occuredOn() {
        return this.occuredOn;
    }
}
