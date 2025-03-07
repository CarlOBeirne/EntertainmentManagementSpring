package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryPlaylistDao;
import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.service.impl.PlaylistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceImplTest {

    @Mock
    private InMemoryPlaylistDao playlistDao;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    private Playlist playlist;
    private Track track;

    @BeforeEach
    void setUp() {
        playlist = new Playlist(List.of(), "Test Playlist", "A test playlist");
        playlist.setId(1L);
        track = new Track("Test Track", 200, null, List.of(), 2023, 120);
        track.setId(10L);
    }

    @Test
    void testGetAllPlaylists() {
        when(playlistDao.findAllPlaylist()).thenReturn(List.of(playlist));
        List<Playlist> playlists = playlistService.getAllPlaylists();
        assertFalse(playlists.isEmpty());
        assertEquals(1, playlists.size());
        assertEquals("Test Playlist", playlists.get(0).getTitle());
    }

    @Test
    void testGetPlaylistById_NotFound() {
        when(playlistDao.findPlaylistById(2L)).thenReturn(Optional.empty());
        Optional<Playlist> result = playlistService.getPlaylistById(2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreatePlaylist() {
        playlistService.createPlaylist(playlist);
        verify(playlistDao, times(1)).save(playlist);
    }

    @Test
    void testEditPlaylist() {
        when(playlistDao.findPlaylistById(1L)).thenReturn(Optional.of(playlist));
        Playlist updatedPlaylist = new Playlist(List.of(), "Updated Title", "Updated Description");
        playlistService.editPlaylist(1L, updatedPlaylist);
        verify(playlistDao, times(1)).editPlaylist(1L, updatedPlaylist);
    }

    @Test
    void testDeletePlaylist() {
        when(playlistDao.findPlaylistById(1L)).thenReturn(Optional.of(playlist));
        playlistService.deletePlaylist(1L);
        verify(playlistDao, times(1)).deletePlaylist(1L);
    }

    @Test
    void testAddTrackToPlaylist() {
        when(playlistDao.findPlaylistById(1L)).thenReturn(Optional.of(playlist));
        playlistService.addTrackToPlaylist(1L, track);
        verify(playlistDao, times(1)).addTrackToPlaylist(1L, track);
    }

    @Test
    void testRemoveTrackFromPlaylist() {
        when(playlistDao.findPlaylistById(1L)).thenReturn(Optional.of(playlist));
        playlistService.removeTrackFromPlaylist(1L, 10L);
        verify(playlistDao, times(1)).removeTrackFromPlaylist(1L, 10L);
    }
}