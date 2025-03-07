package com.pluralsight.EntertainmentManagementSpring.dao.inmemory;

import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@Tag("unit")
    public class InMemoryTrackDAOTest {

    private InMemoryTrackDAO trackDAO = new InMemoryTrackDAO();
    private Track track1;
    private Track track2;

    @BeforeEach
    public void setUp() {
        trackDAO = new InMemoryTrackDAO();
        trackDAO.resetDatastore();
        track1 = createTestTrack( "Roxanne", String.valueOf(Genre.ROCK), 120);
        track2 = createTestTrack( "Wasted Youth", String.valueOf(Genre.ELECTRONIC), 180);
        trackDAO.create(track1);
        trackDAO.create(track2);
    }

    @Test
    public void testFindByIdTrack() {
        Optional<Track> testTrack = trackDAO.findById(track1.getId());
        assertTrue(testTrack.isPresent());
        assertEquals(1L, testTrack.get().getId());
    }

    private Track createTestTrack( String title, String genre, int duration) {
        Track track = new Track();
        track.setTitle(title);
        track.setGenre(Genre.valueOf(genre));
        track.setDurationSeconds(duration);
        return track;
    }

    @Test
    public void testFindAllTrack() {
        List<Track> tracks = trackDAO.findAll();
        assertFalse(tracks.isEmpty());
        assertEquals(tracks.size(), 2);
    }

    @Test
    public void testUpdateTrack() {
        //Arrange
        Track testTrack = trackDAO.findById(track1.getId()).orElseThrow();
        testTrack.setTitle("Test");
        testTrack.setGenre(track1.getGenre());
        testTrack.setDurationSeconds(track1.getDurationSeconds());

        Track updatedTrack = trackDAO.update(testTrack);
        boolean updated = updatedTrack != null;
        //Assert
        assertTrue(updated);
        Optional<Track> track = trackDAO.findById(track1.getId());
        assertTrue(track.isPresent());
        assertEquals("Test", track.get().getTitle(), "Title Updated");
    }

    @Test
    public void testDeleteTrack() {
        //we have 2 tracks from setUp
        boolean deleted = trackDAO.delete(1L);
        List<Track> tracks = trackDAO.findAll();
        assertAll(
                () -> assertTrue(deleted),
                () -> assertNotNull(tracks),
                () -> assertFalse(tracks.isEmpty()),
                () -> assertEquals(1, tracks.size()),
                () -> assertTrue(tracks.contains(track2))
        );
    }

    @Test
    public void testDeleteAllTrack() {
        trackDAO.resetDatastore();

    }
    //TODO ??
    @Test
    public void testAttemptToUpdateWrongTrack() {
        Track wrongTrack = Track.builder().build();
        wrongTrack.setId(9L);
        Track updated = trackDAO.update(wrongTrack);
        assertNull(updated, "Expecting null");
    }

    @Test
    public void testAttemptToDeleteNonExistentTrack() {
        // Arrange
        Track testTrack = Track.builder().build();
        testTrack.setId(-1L);
        // Act
        boolean deleted = trackDAO.delete(9L);
        // Assert
        assertFalse(deleted);
    }
}
