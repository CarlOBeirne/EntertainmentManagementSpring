package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryTrackDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")

public class TrackDataServiceTest {

    @Mock
    private InMemoryTrackDAO inMemoryTrackDAO;

    @Mock
    private ArtistDataService artistDataService;

    @Mock
    private ApplicationEventPublisher trackPublisher;

    @InjectMocks
    private TrackDataService trackDataService;
    private Track testTrack;
    private Artist testArtist;

    @BeforeEach
    void setUp() {
        testTrack = Track.builder()
                .title("Roxanne")
                .durationSeconds(120)
                .genre(Genre.ROCK)
                .yearReleased(1980)
                .beatsPerMinute(120)
                .build();
    }

    @Test
    public void TestFindAllTracks() {
        //Arrange
        Mockito.when(inMemoryTrackDAO.findAll()).thenReturn(List.of(testTrack));
        //Act
        List<Track> results = trackDataService.getAllTracks();

        //Assert
        assertAll(
                () -> assertNotNull(results),
                () -> assertEquals(1, results.size()),
                () -> assertEquals(testTrack, results.getFirst())
        );

        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindAllTracks_NoTracks() {
        //Arrange
        Mockito.when(inMemoryTrackDAO.findAll()).thenReturn(Collections.emptyList());

        //Act
        List<Track> results = trackDataService.getAllTracks();
        assertAll(
                () -> assertNotNull(results),
                () -> assertTrue(results.isEmpty())
        );
        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).findAll();

    }

    @Test
    public void TestFindTrackById_NotFound() {
        //Arrange
        Mockito.when(inMemoryTrackDAO.findById(999L)).thenReturn(Optional.empty());
        //Act
        Track testTrack = trackDataService.getByTrackId(999L);
        //Assert
        assertNull(testTrack);
        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).findById(999L);
    }

    @Test
    public void TestUpdateTrack() {
        //Arrange
        Track updatedTrack = testTrack.toBuilder()
                .title("California Dreaming")
                .durationSeconds(250)
                .genre(Genre.ROCK)
                .yearReleased(1990)
                .beatsPerMinute(100)
                .build();

        Mockito.when(inMemoryTrackDAO.findById(testTrack.getId())).thenReturn(Optional.of(testTrack));
        Mockito.when(inMemoryTrackDAO.update(Mockito.any(Track.class))).thenReturn(updatedTrack);

        //Act
        Track result = trackDataService.updateTrack(updatedTrack);

        //Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("California Dreaming", result.getTitle()),
                () -> assertEquals(250, result.getDurationSeconds()),
                () -> assertEquals(Genre.ROCK, result.getGenre()),
                () -> assertEquals(1990, result.getYearReleased()),
                () -> assertEquals(100, result.getBeatsPerMinute())
        );
    }

    @Test
    public void TestUpdateTrack_NotFound() {
        Track updatedTrack = testTrack.toBuilder()
                .id(999L)
                .title("California Dreaming")
                .durationSeconds(250)
                .genre(Genre.ROCK)
                .yearReleased(1990)
                .beatsPerMinute(100)
                .build();

        Mockito.when(inMemoryTrackDAO.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> trackDataService.updateTrack(updatedTrack));

        assertEquals("Track not found", exception.getMessage());

        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).findById(999L);
        Mockito.verify(inMemoryTrackDAO, Mockito.never()).update(Mockito.any(Track.class));
    }

    @Test
    public void TestDeleteTrack() {
        Mockito.when(inMemoryTrackDAO.findById(testTrack.getId())).thenReturn(Optional.ofNullable(testTrack));
        Mockito.when(inMemoryTrackDAO.delete(testTrack.getId())).thenReturn(true);

        //Act
        boolean result = trackDataService.deleteTrack(testTrack.getId());

        //Assert
        assertTrue(result);

        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).findById(testTrack.getId());
        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).delete(testTrack.getId());
    }

    @Test
    public void TestDeleteTrack_NotFound() {
        //arrange
        Mockito.when(inMemoryTrackDAO.findById(999L)).thenReturn(Optional.empty());

        //act
        boolean result = trackDataService.deleteTrack(999L);

        //assert
        assertFalse(result);

        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).findById(999L);
        Mockito.verify(inMemoryTrackDAO, Mockito.never()).delete(999L);
    }

    @Test
    public void TestCreateTrack() {
        //Arrange (because 1L is made)

        Mockito.when(inMemoryTrackDAO.create(Mockito.any(Track.class))).thenAnswer(invocation -> {
            Track track = invocation.getArgument(0);
            track.setId(2L);
            return track;
        });

        //Act
        Track createdTrack = trackDataService.createTrack(testTrack);

        //assert
        assertEquals(createdTrack, testTrack);
        assertNotNull(createdTrack);
        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).create(testTrack);

    }
}
