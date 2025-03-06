package com.pluralsight.EntertainmentManagementSpring.service;


import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;

import java.util.List;

public interface PlaylistService {
    List<Track> getAllTracks(Long playlistId);
    List<Playlist> getAllPlaylists();
    List<Playlist> getPlaylistByName(String playlistName);
    List<Track> getTrackByName(Long playlistId, String trackName);
    int getTrackCount(Long playlistId);
    int getPlaylistCount(Long playlistId);
    Track getTrackById(Long trackId);
    Playlist getPlaylistById(Long playlistId);
    void createPlaylist(Playlist playlist);
    void editPlaylist(Playlist playlist);
    void deletePlaylist(Long playlistId);
    void addTrackToPlaylist(Long playlistId, Track track);
    void removeTrackFromPlaylist(Long playlistId, Track track);
}
