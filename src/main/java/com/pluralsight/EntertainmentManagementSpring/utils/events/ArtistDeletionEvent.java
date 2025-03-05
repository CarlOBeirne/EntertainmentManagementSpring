package com.pluralsight.EntertainmentManagementSpring.utils.events;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class ArtistDeletionEvent extends ApplicationEvent {

    private Long artistId;
    private List<Long> trackIds;

    public ArtistDeletionEvent(Object source, Long artistId, List<Long> trackIds) {
        super(source);
        this.artistId = artistId;
        this.trackIds = trackIds;
    }

    public List<Long> getTrackIds() {
        return trackIds;
    }

    public Long getArtistId() {
        return artistId;
    }
}
