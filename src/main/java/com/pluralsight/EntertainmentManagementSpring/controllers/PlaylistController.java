package com.pluralsight.EntertainmentManagementSpring.controllers;

import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/{playlistId}/tracks")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Track>> getAllTracksFromPlaylist(@PathVariable Long playlistId) {
        return playlistService.getAllTracksFromPlaylist(playlistId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Playlist> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/getByName")
    @ResponseStatus(HttpStatus.OK)
    public List<Playlist> getPlaylistByName(@RequestParam String playlistName) {
        return playlistService.getPlaylistByName(playlistName);
    }

    @GetMapping("/{playlistId}/tracks-by-name")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Track>> getPlaylistTracksByName(@PathVariable Long playlistId, @RequestParam String trackName) {
        return playlistService.getPlaylistTracksByName(playlistId, trackName);
    }
    @GetMapping("/{playlistId}/{trackId}/track")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Track> getPlaylistTrackById(@PathVariable Long playlistId, @PathVariable Long trackId) {
        return playlistService.getPlaylistTrackById(playlistId, trackId);
    }

    @GetMapping("{playlistId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long playlistId) {
        return playlistService.getPlaylistById(playlistId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlaylist(@RequestBody Playlist playlist) {
        playlistService.createPlaylist(playlist);
    }

    @PutMapping("/{playlistId}/edit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editPlaylist(@PathVariable Long playlistId,@RequestBody Playlist updatedPlaylist) {
        playlistService.editPlaylist(playlistId, updatedPlaylist);
    }

    @DeleteMapping("/{playlistId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
    }

    @PutMapping("add/{playlistId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addTrackToPlaylist(@PathVariable Long playlistId, @RequestBody Track track) {
        playlistService.addTrackToPlaylist(playlistId, track);
    }
    @PutMapping("remove/{playlistId}/{TrackId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTrackFromPlaylist(@PathVariable Long playlistId, @PathVariable Long TrackId) {
        playlistService.removeTrackFromPlaylist(playlistId, TrackId);
    }

}
