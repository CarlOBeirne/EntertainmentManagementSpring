package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.utils.events.ArtistDeletionEvent;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.InvalidArtistIdException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NoArtistFoundException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NullArtistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistDataServiceTest {

    @Mock
    private InMemoryDAO<Artist> inMemoryArtistDAOMock;

    @Mock
    private ApplicationEventPublisher applicationEventPublisherMock;

    @InjectMocks
    private ArtistDataService artistDataService;

    @Spy
    private List<Long> trackIds = new ArrayList<>();

    @Captor
    private ArgumentCaptor<Long> artistIdCaptor;

    @Test
    void findArtistById_shouldReturnArtistMatchingId() {
        // Arrange
        Artist artist = Artist.builder().id(1L).build();
        when(inMemoryArtistDAOMock.findById(artist.getId())).thenReturn(Optional.of(artist));

        // Act
        Optional<Artist> optionalArtist = artistDataService.findArtistById(artist.getId());

        // Assert
        assertNotNull(optionalArtist);
        assertTrue(optionalArtist.isPresent());
        assertEquals(artist, optionalArtist.get());
    }

    @Test
    void findArtistById_shouldReturnEmptyOptionalIfArtistDoesNotExist() {
        // Arrange
        when(inMemoryArtistDAOMock.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Artist> optionalArtist = artistDataService.findArtistById(1L);

        // Assert
        assertTrue(optionalArtist.isEmpty());

    }

    @Test
    void findArtistById_shouldThrowExceptionIfNullIdIsPassed() {
        InvalidArtistIdException invalidArtistIdException = assertThrows(InvalidArtistIdException.class, () -> artistDataService.findArtistById(null));
        String exceptionMessage = invalidArtistIdException.getMessage();
        assertEquals("Artist id cannot be null", exceptionMessage);
        verify(inMemoryArtistDAOMock, times(0)).findById(anyLong());

    }

    @Test
    void findAllArtists_shouldReturnAllArtists() {
        List<Artist> artists = List.of(new Artist(), new Artist(), new Artist(), new Artist());
        // Arrange
        when(inMemoryArtistDAOMock.findAll()).thenReturn(artists);

        // Act
        artistDataService.findAllArtists();

        // Assert
        verify(inMemoryArtistDAOMock, times(1)).findAll();
    }

    @Test
    void saveArtist_shouldThrowExceptionIfArtistIsNull() {
        NullArtistException nullArtistException = assertThrows(NullArtistException.class, () -> artistDataService.saveArtist(null));
        String exceptionMessage = nullArtistException.getMessage();
        assertEquals("Artist cannot be null", exceptionMessage);
        verify(inMemoryArtistDAOMock, times(0)).create(any());
        verify(inMemoryArtistDAOMock, times(0)).update(any());
    }

    @Test
    void saveArtist_shouldCreateNewArtistWhenIdIsNull() {
        // Arrange
        Artist artist = Artist.builder().id(null).build();
        when(inMemoryArtistDAOMock.create(artist)).thenReturn(artist);

        // Act
        Artist persistedArtist = artistDataService.saveArtist(artist);

        // Assert
        verify(inMemoryArtistDAOMock, times(1)).create(persistedArtist);
        verify(inMemoryArtistDAOMock, times(0)).update(any());
        assertNotNull(persistedArtist);
    }

    @Test
    void saveArtist_shouldUpdateArtistWhenIdIsNotNullAndIdExistsInDatastore() {
        // Arrange
        Artist artist = Artist.builder().id(1L).yearFounded(2025).build();
        assertNotNull(artist);
        when(inMemoryArtistDAOMock.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(inMemoryArtistDAOMock.update(artist)).thenReturn(artist);

        // Act
        Artist persistedArtist = artistDataService.saveArtist(artist);

        // Assert
        assertNotNull(persistedArtist);
        verify(inMemoryArtistDAOMock, times(0)).create(any());
        verify(inMemoryArtistDAOMock, times(1)).update(any());
    }

    @Test
    void saveArtist_shouldThrowExceptionIfArtistIdIsPresentButNotInDatastore() {
        Artist artist = Artist.builder().id(1L).build();
        NoArtistFoundException noArtistFoundException = assertThrows(NoArtistFoundException.class, () -> artistDataService.saveArtist(artist));
        String exceptionMessage = noArtistFoundException.getMessage();
        assertTrue(exceptionMessage.startsWith("No artist found with id "));
    }

    @Test
    void deleteArtistById_shouldThrowExceptionIfArtistIdIsNull() {
        InvalidArtistIdException invalidArtistIdException = assertThrows(InvalidArtistIdException.class, () -> artistDataService.deleteArtistById(null));
        String exceptionMessage = invalidArtistIdException.getMessage();
        assertTrue(exceptionMessage.startsWith("Artist id cannot be null"));
    }

    @Test
    void deleteArtistById_shouldDeleteArtistAndReturnTrueWhenIdIsNotNullAndIdExistsInDatastore() {
        // Arrange
        Artist artist = Artist.builder().id(1L).build();
        assertNotNull(artist);
        when(inMemoryArtistDAOMock.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(inMemoryArtistDAOMock.delete(artist.getId())).thenReturn(true);

        // Act
        boolean deleteArtistById = artistDataService.deleteArtistById(artist.getId());

        // Assert
        verify(applicationEventPublisherMock, times(1)).publishEvent(any(ArtistDeletionEvent.class));
        verify(inMemoryArtistDAOMock, times(1)).findById(artist.getId());
        verify(inMemoryArtistDAOMock, times(1)).delete(artist.getId());
        assertTrue(deleteArtistById);

    }

    @Test
    void deleteArtistById_shouldIterateThroughListOfTracksAndAddToList() {
        Track track1 = Track.builder().id(1L).build();
        Track track2 = Track.builder().id(2L).build();
        Artist artist = Artist.builder().track(track1).track(track2).build();
        assertNotNull(artist);
        assertEquals(2, artist.getTracks().size());
        when(inMemoryArtistDAOMock.findById(artistIdCaptor.capture())).thenReturn(Optional.of(artist));
        when(inMemoryArtistDAOMock.delete(artistIdCaptor.capture())).thenReturn(true);

        artistDataService.deleteArtistById(artistIdCaptor.capture());

        for (Track track : artist.getTracks()) {
            trackIds.add(track.getId());
        }

        verify(trackIds).add(track1.getId());
        verify(trackIds).add(track2.getId());
    }

    @Test
    void deleteArtistById_shouldReturnFalseWhenNoArtistIsPresentToBeDeleted() {
        assertFalse(artistDataService.deleteArtistById(1L));
        verify(inMemoryArtistDAOMock, times(1)).findById(anyLong());
        verify(inMemoryArtistDAOMock, times(0)).delete(anyLong());
    }

    @Test
    void isExistingArtist_shouldReturnTrueIfArtistExists() {
        Artist artist = Artist.builder().id(1L).build();
        when(inMemoryArtistDAOMock.findById(anyLong())).thenReturn(Optional.ofNullable(artist));
        assertTrue(artistDataService.isExistingArtist(artist));
    }

    @Test
    void isExistingArtist_shouldReturnFalseIfArtistDoesNotExist() {
        Artist artist = Artist.builder().id(1L).build();
        when(inMemoryArtistDAOMock.findById(anyLong())).thenReturn(Optional.empty());
        assertFalse(artistDataService.isExistingArtist(artist));
    }

    @Test
    void validArtistObjectCheck_shouldThrowExceptionIfArtistIsNull() {
        NullArtistException nullArtistException = assertThrows(NullArtistException.class, () -> ArtistDataService.validArtistObjectCheck(null));
        String exceptionMessage = nullArtistException.getMessage();
        assertEquals("Artist cannot be null", exceptionMessage);
    }

    @Test
    void validArtistObjectCheck_shouldNotThrowExceptionIfArtistIsPresent() {
        Artist artist = Artist.builder().id(1L).build();
        assertDoesNotThrow(() -> ArtistDataService.validArtistObjectCheck(artist));
    }

    @Test
    void nullIdCheck_shouldThrowExceptionIfIdIsNull() {
        InvalidArtistIdException invalidArtistIdException = assertThrows(InvalidArtistIdException.class, () -> ArtistDataService.nullIdCheck(null));
        assertNotNull(invalidArtistIdException);
        String exceptionMessage = invalidArtistIdException.getMessage();
        assertEquals("Artist id cannot be null", exceptionMessage);
    }

    @Test
    void nullIdCheck_shouldNotThrowExceptionIfArtistIdIsNotNull() {
        Artist artist = Artist.builder().id(1L).build();
        assertDoesNotThrow(() -> ArtistDataService.nullIdCheck(artist.getId()));
    }
}