package com.pluralsight.EntertainmentManagementSpring.controllers;


import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.service.TrackDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks")

public class TrackController {

    private final TrackDataService trackDataService;

    //TODO check for nulls

    @GetMapping
    public ResponseEntity<List<Track>> getAllTracks() {
        List<Track> tracks = trackDataService.getAllTracks();
        if (tracks == null || tracks.isEmpty()) {
            return ResponseEntity.noContent().build(); //204
        }
        return ResponseEntity.ok(trackDataService.getAllTracks()); //200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<Track> getTrackById(@PathVariable Long id) {
        return ResponseEntity.ok(trackDataService.getByTrackId(id));
    }

    @PostMapping
    public ResponseEntity<Track> createTrack(@RequestBody Track track) {
        return ResponseEntity.ok(trackDataService.createTrack(track));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Track> updateTrack(@RequestBody Track track, @PathVariable Long id) {
        if (!Objects.equals(track.getId(), id)) {
            return ResponseEntity.badRequest().build();
        }
        Track updatedTrack = trackDataService.updateTrack(track);
        return updatedTrack != null ? ResponseEntity.ok(updatedTrack) : ResponseEntity.notFound().build(); //200 OK / 404 Not found
        //boolean result = trackDataService.updateTrack(id);
        //return result ? ResponseEntity.ok(id) : ResponseEntity.notFound().build();
        //return ResponseEntity.ok(trackDataService.updateTrack(track));
    }
    //202 if ok, 404 if wrong
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id) {
        boolean result = trackDataService.deleteTrack(id);
        return result ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }


















}
