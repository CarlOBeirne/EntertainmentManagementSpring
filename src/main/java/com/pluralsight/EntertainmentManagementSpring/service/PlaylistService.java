package com.pluralsight.EntertainmentManagementSpring.service;


import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import java.util.List;
import java.util.Optional;

public interface PlaylistService {
    Optional<List<Track>> getAllTracksFromPlaylist(Long playlistId);
    List<Playlist> getAllPlaylists();
    List<Playlist> getPlaylistByName(String playlistName);
    Optional<List<Track>> getPlaylistTracksByName(Long playlistId, String trackName);
    Optional<Track> getPlaylistTrackById(Long playlistId, Long trackId);
    Optional<Playlist> getPlaylistById(Long playlistId);
    void createPlaylist(Playlist playlist);
    void editPlaylist(Long playlistId, Playlist updatedPlaylist);
    void deletePlaylist(Long playlistId);
    void addTrackToPlaylist(Long playlistId, Track track);
    void removeTrackFromPlaylist(Long playlistId, Long trackId);
}
