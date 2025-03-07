package com.pluralsight.EntertainmentManagementSpring.service.impl;

import com.pluralsight.EntertainmentManagementSpring.config.AppPlaylistMessages;
import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryPlaylistDao;
import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.service.PlaylistService;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.IdNotNullException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NameNotNullException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.PlaylistNotFoundException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.TitleNotNullException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private InMemoryPlaylistDao playlistDao;


    @Override
    public Optional<List<Track>> getAllTracksFromPlaylist(Long playlistId) {
        Optional<List<Track>> tracks = playlistDao.findAllPlaylistTracks(playlistId);
        if (tracks == null) {
            throw new PlaylistNotFoundException(AppPlaylistMessages.PLAYLIST_NOT_FOUND_ERROR);
        }if (playlistId == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_ID_NOT_NULL_ERROR);
        }
        return tracks;
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return playlistDao.findAllPlaylist();
    }

    @Override
    public List<Playlist> getPlaylistByName(String playlistName) {
        List<Playlist> playlists = playlistDao.findPlaylistByName(playlistName);
        if (playlists == null) {
            throw new PlaylistNotFoundException(AppPlaylistMessages.PLAYLIST_NOT_FOUND_ERROR);
        }if (playlistName == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_NAME_NOT_NULL_ERROR);
        }
        return playlists;
    }

    @Override
    public Optional<List<Track>> getPlaylistTracksByName(Long playlistId, String trackName) {
        Optional<List<Track>> tracks = playlistDao.findPlaylistTrack(playlistId, trackName);
        if (trackName == null) {
            throw new NameNotNullException(AppPlaylistMessages.TRACK_NAME_NOT_NULL_ERROR);
        } if (playlistId == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_ID_NOT_NULL_ERROR);
        } if (tracks == null) {
            throw new PlaylistNotFoundException(AppPlaylistMessages.TRACK_NOT_FOUND_ERROR);
        }
        return tracks;
    }

    @Override
    public Optional<Track> getPlaylistTrackById(Long playlistId, Long trackId) {
        Optional<Track> track = playlistDao.findPlaylistTrackById(playlistId, trackId);
        if (track == null) {
            throw new PlaylistNotFoundException(AppPlaylistMessages.TRACK_NOT_FOUND_ERROR);
        } if (trackId == null) {
            throw new IdNotNullException(AppPlaylistMessages.TRACK_ID_NOT_NULL_ERROR);
        } if (playlistId == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_ID_NOT_NULL_ERROR);
        }
        return track;
    }

    @Override
    public Optional<Playlist> getPlaylistById(Long playlistId) {
        Optional<Playlist> playlist = playlistDao.findPlaylistById(playlistId);
        if (playlist == null) {
            throw new PlaylistNotFoundException(AppPlaylistMessages.PLAYLIST_NOT_FOUND_ERROR);
        } if (playlistId == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_ID_NOT_NULL_ERROR);
        }
        return playlist;
    }

    @Override
    public void createPlaylist(Playlist playlist) {
        if (playlist == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_NOT_NULL_ERROR);
        }if (playlist.getTitle() == null) {
            throw new TitleNotNullException(AppPlaylistMessages.TITLE_NULL_ERROR);
        }
        playlistDao.save(playlist);
    }

    @Override
    public void editPlaylist(Long playlistId, Playlist updatedPlaylist) {
        if (updatedPlaylist == null) {
            throw new PlaylistNotFoundException(AppPlaylistMessages.PLAYLIST_NOT_FOUND_ERROR);
        } if (playlistId == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_ID_NOT_NULL_ERROR);
        }
        playlistDao.editPlaylist(playlistId, updatedPlaylist);
    }

    @Override
    public void deletePlaylist(Long playlistId) {
        if (playlistId == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_ID_NOT_NULL_ERROR);
        } if (playlistDao.findPlaylistById(playlistId) == null) {
            throw new PlaylistNotFoundException(AppPlaylistMessages.PLAYLIST_NOT_FOUND_ERROR);
        }
        playlistDao.deletePlaylist(playlistId);
    }

    @Override
    public void addTrackToPlaylist(Long playlistId, Track track) {
        if (playlistId == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_ID_NOT_NULL_ERROR);
        } if (playlistDao.findPlaylistById(playlistId) == null) {
            throw new PlaylistNotFoundException(AppPlaylistMessages.PLAYLIST_NOT_FOUND_ERROR);
        } if (track == null) {
            throw new IdNotNullException(AppPlaylistMessages.TRACK_NOT_FOUND_ERROR);
        }
        playlistDao.addTrackToPlaylist(playlistId, track);
    }

    @Override
    public void removeTrackFromPlaylist(Long playlistId, Long trackId) {
        if (playlistId == null) {
            throw new IdNotNullException(AppPlaylistMessages.PLAYLIST_ID_NOT_NULL_ERROR);
        } if (trackId == null) {
            throw new IdNotNullException(AppPlaylistMessages.TRACK_NOT_FOUND_ERROR);
        }
        playlistDao.removeTrackFromPlaylist(playlistId, trackId);
    }
}
