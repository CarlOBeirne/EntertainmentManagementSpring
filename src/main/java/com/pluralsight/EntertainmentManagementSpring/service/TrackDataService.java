package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.utils.events.ArtistDeletionEvent;
import com.pluralsight.EntertainmentManagementSpring.utils.events.TrackCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TrackDataService {

    private final InMemoryDAO<Track> trackDAO;
    private final InMemoryDAO<Artist> artistDAO;

    private final ApplicationEventPublisher trackPublisher;

    public Track createTrack(Track track) {
        Track persistedTrack = trackDAO.create(track);
        List<Long> artistIds = persistedTrack.getArtists().stream().map(Artist::getId).toList();
        trackPublisher.publishEvent(new TrackCreatedEvent(this, persistedTrack.getId(), artistIds));
        return persistedTrack;
    }
    public Track getByTrackId(Long id) { return trackDAO.findById(id).orElse(null); }
    public List<Track> getAllTracks() {
        return trackDAO.findAll();
    }

    public Track updateTrack(Track track) {
        Optional<Track> existingTrack = trackDAO.findById(track.getId());
        //.isPresent() && existingTrack
        return existingTrack
                .map(t -> {
                    t.setId(track.getId());
                    t.setTitle(track.getTitle());
                    t.setDurationSeconds(track.getDurationSeconds());
                    t.setGenre(track.getGenre());
                    t.setArtists(track.getArtists());
                    t.setYearReleased(track.getYearReleased());
                    t.setBeatsPerMinute(track.getBeatsPerMinute());
                    return trackDAO.update(t);
                })
                .orElseThrow(() -> new NoSuchElementException("Track not found"));
    }

    public boolean deleteTrack(Long id) {
        Optional<Track> existingTrack = trackDAO.findById(id);
        if (existingTrack.isPresent()) {
            return trackDAO.delete(id);
        }
        return false;
    }

    @EventListener
    public void handleArtistDeletionEvent(ArtistDeletionEvent artistDeletionEvent) {
        Optional<Artist> optionalArtist = artistDAO.findById(artistDeletionEvent.getArtistId());
        if (optionalArtist.isPresent()) {
            Artist artist = optionalArtist.get();
            ArrayList<Track> trackList = new ArrayList<>();
            artistDeletionEvent.getTrackIds()
                    .forEach(trackId -> trackDAO.findById(trackId).ifPresent(trackList::add));
            for (Track track : trackList) {
                track.removeArtist(artist);
            }
        }
    }

}