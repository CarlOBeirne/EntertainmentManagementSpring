package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryTrackDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class TrackDataService {
    private final InMemoryTrackDAO<Track> trackDAO;

    public Track createTrack(Track track) { return trackDAO.create(track); }
    public Track getByTrackId(Long id) { return trackDAO.findById(id).orElse(null); }
    public List<Track> getAllTracks() {
        return trackDAO.findAll();
    }
    //public List<Track> getAllTracks() {
    //    return Optional.ofNullable(trackDAO.findAll()).orElse(Collections.emptyList());
    //}

    public Track updateTrack(Track track) {
        Optional<Track> existingTrack = trackDAO.findById(track.getId());
        //.isPresent() && existingTrack
        return existingTrack
                .map(t -> {
                    t.setId(track.getId());
                    t.setTitle(track.getTitle());
                    t.setDurationSeconds(track.getDurationSeconds());
                    t.setGenre(track.getGenre());
                    t.setTracks(track.getTracks());
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

}

//private Long id;
//private String title;
//private int durationSeconds;
//private Genre genre;
//private List<Track> tracks;
//private int yearReleased;
//private int beatsPerMinute;