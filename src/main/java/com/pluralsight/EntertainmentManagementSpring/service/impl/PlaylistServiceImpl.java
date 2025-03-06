package com.pluralsight.EntertainmentManagementSpring.service.impl;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryDAO;
import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryPlaylistDao;
import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private InMemoryDAO<Playlist> playlistDao;

    @Override
    public List<Track> getAllTracks(Long playlistId) {
        Optional<Playlist> playlist = playlistDao.findById(playlistId);
        return playlist.map(Playlist::getTracks).orElse(List.of());
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlist = playlistDao.findAll();
        return playlist;
    }

    @Override
    public List<Playlist> getPlaylistByName(String playlistName) {
        return List.of();
    }

    @Override
    public List<Track> getTrackByName(Long playlistId, String trackName) {
        return List.of();
    }

    @Override
    public int getTrackCount(Long playlistId) {
        return 0;
    }

    @Override
    public int getPlaylistCount(Long playlistId) {
        return 0;
    }

    @Override
    public Track getTrackById(Long trackId) {
        return null;
    }

    @Override
    public Playlist getPlaylistById(Long playlistId) {
        return null;
    }

    @Override
    public void createPlaylist(Playlist playlist) {

    }

    @Override
    public void editPlaylist(Playlist playlist) {

    }

    @Override
    public void deletePlaylist(Long playlistId) {

    }

    @Override
    public void addTrackToPlaylist(Long playlistId, Track track) {

    }

    @Override
    public void removeTrackFromPlaylist(Long playlistId, Track track) {

    }
}
