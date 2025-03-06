package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryTrackDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.enums.ArtistType;
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
import static org.mockito.Mockito.*;

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

    //@Test
    //public void TestFindTrackById() {
    //    //Arrange
    //    Mockito.when(inMemoryTrackDAO.findById(1L)).thenReturn(Optional.of(testTrack));
    //    //Act
    //    Track testTrack = trackDataService.getByTrackId(1L);
    //    //Assert
    //    assertAll(
    //            () -> assertNotNull(testTrack),
    //            () -> assertEquals(1L, testTrack.getId())
    //    );
    //    Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).findById(1L);
    //}

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

        //Mockito.when(inMemoryTrackDAO.create(testTrack)).thenReturn(testTrack);
        //Mockito.when(inMemoryTrackDAO.findById(1L)).thenReturn(Optional.ofNullable(testTrack));

        //Act
        Track createdTrack = trackDataService.createTrack(testTrack);

        //Track resultTrack = trackDataService.getByTrackId(createdTrack.getId());

        //assert
        assertEquals(createdTrack, testTrack);
        assertNotNull(createdTrack);
        Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).create(testTrack);
        //Mockito.verify(inMemoryTrackDAO, Mockito.times(1)).findById(1L);

    }












}

//return Optional.ofNullable(datastore.get(id))


    //@BeforeEach
    //void setUp() {
    //    testArtist = new Artist();
    //    testTrack = createTestTrack(1L, "Bohemian Rhapsody", Genre.ROCK, 354, List.of(testArtist));
//
    //}

   //private Track createTestTrack( String title, String genre, int duration) {
   //    Track track = new Track();
   //    track.setTitle(title);
   //    track.setGenre(Genre.valueOf(genre));
   //    track.setDurationSeconds(duration);
   //    return track;
    //}
//
    //private Track createTestTrack(Long id, String title, Genre genre, int duration, List<Artist> artists) {
    //    Track track = new testTrack(id, title, genre, duration);
    //    track.setArtists(artists);
    //    return track;
    //}
    //    @EqualsAndHashCode.Include
    //    private String name;
    //
    //    @EqualsAndHashCode.Include
    //    private ArtistType artistType;
    //
    //    @Singular
    //    private List<Genre> genres;
    //
    //    private String biography;
    //
    //    @EqualsAndHashCode.Include
    //    private String nationality;
    //
    //    @EqualsAndHashCode.Include
    //    private int yearFounded;
    //
    //    @JsonIgnore
    //    @ToString.Exclude
    //    @Singular
    //    private List<Track> tracks;



    //@BeforeEach
    //public void setUp() {
    //    track1 = Track.builder()
    //            .id(1l)
    //            .title("Roxanne")
    //            .genre(Genre.valueOf(String.valueOf(Genre.ROCK)))
    //            .durationSeconds((120)
    //                    .build();
//
//
//
    //}


//"Roxanne", String.valueOf(Genre.ROCK), 120);


//}
