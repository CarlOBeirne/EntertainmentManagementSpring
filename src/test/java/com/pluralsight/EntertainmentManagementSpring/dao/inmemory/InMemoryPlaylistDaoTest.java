package com.pluralsight.EntertainmentManagementSpring.dao.inmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;

public class InMemoryPlaylistDaoTest {

    private InMemoryPlaylistDao playlistDao;

    @BeforeEach
    void setUp() {
        playlistDao = new InMemoryPlaylistDao();
    }

    @Test
    void testSavePlaylist() {
        Playlist playlist = new Playlist(List.of(), "Rock Classics", "Best rock songs");
        playlistDao.save(playlist);
        assertNotNull(playlist.getId());
        assertEquals(1, playlistDao.findAllPlaylist().size());
    }

    @Test
    void testFindPlaylistById_NotFound() {
        Optional<Playlist> result = playlistDao.findPlaylistById(99L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindPlaylistByName() {
        Playlist playlist = new Playlist(List.of(), "Workout", "Gym music");
        playlistDao.save(playlist);
        List<Playlist> results = playlistDao.findPlaylistByName("Workout");
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void testDeletePlaylist() {
        Playlist playlist = new Playlist(List.of(), "Chill Vibes", "Relaxing music");
        playlistDao.save(playlist);
        playlistDao.deletePlaylist(playlist.getId());
        assertTrue(playlistDao.findAllPlaylist().isEmpty());
    }

    @Test
    void testEditPlaylist() {
        Playlist playlist = new Playlist(List.of(), "Old Title", "Description");
        playlistDao.save(playlist);
        Playlist updatedPlaylist = new Playlist(List.of(), "New Title", "New Description");
        playlistDao.editPlaylist(playlist.getId(), updatedPlaylist);
        Optional<Playlist> result = playlistDao.findPlaylistById(playlist.getId());
        assertTrue(result.isPresent());
        assertEquals("New Title", result.get().getTitle());
    }

    @Test
    void testAddTrackToPlaylist() {
        Playlist playlist = new Playlist(List.of(), "Party Hits", "Best songs for party");
        playlistDao.save(playlist);
        Track track = new Track("Song 1", 200, null, List.of(), 2023, 120);
        playlistDao.addTrackToPlaylist(playlist.getId(), track);
        Optional<List<Track>> tracks = playlistDao.findAllPlaylistTracks(playlist.getId());
        assertTrue(tracks.isPresent());
        assertEquals(1, tracks.get().size());
    }

    @Test
    void testRemoveTrackFromPlaylist() {
        Playlist playlist = new Playlist(List.of(), "Indie Vibes", "Indie music");
        playlistDao.save(playlist);
        Track track = new Track("Indie Song", 180, null, List.of(), 2023, 110);
        track.setId(1L);
        playlistDao.addTrackToPlaylist(playlist.getId(), track);
        playlistDao.removeTrackFromPlaylist(playlist.getId(), track.getId());
        Optional<List<Track>> tracks = playlistDao.findAllPlaylistTracks(playlist.getId());
        assertTrue(tracks.isPresent());
        assertTrue(tracks.get().isEmpty());
    }
}