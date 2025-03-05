package com.pluralsight.EntertainmentManagementSpring.utils.events;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class TrackCreatedEvent extends ApplicationEvent {
    private Long trackId;
    private List<Long> artistIds;

    public TrackCreatedEvent(Object source, Long trackId, List<Long> artistIds) {
        super(source);
        this.trackId = trackId;
        this.artistIds = artistIds;
    }

    public Long getTrackId() {
        return trackId;
    }

    public List<Long> getArtistIds() {
        return artistIds;
    }
}
