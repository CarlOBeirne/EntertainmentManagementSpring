package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.InvalidArtistIdException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NoArtistFoundException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NullArtistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistDataServiceTest {

    @Mock
    private InMemoryDAO<Artist> inMemoryDAO;

    @InjectMocks
    private ArtistDataService artistDataService;

    @Test
    void findArtistById_shouldReturnArtistMatchingId() {
        // Arrange
        Artist artist = Artist.builder().id(1L).build();
        when(inMemoryDAO.findById(artist.getId())).thenReturn(Optional.of(artist));

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
        when(inMemoryDAO.findById(1L)).thenReturn(Optional.empty());

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
        verify(inMemoryDAO, times(0)).findById(anyLong());

    }

    @Test
    void findAllArtists_shouldReturnAllArtists() {
        List<Artist> artists = List.of(new Artist(), new Artist(), new Artist(), new Artist());
        // Arrange
        when(inMemoryDAO.findAll()).thenReturn(artists);

        // Act
        artistDataService.findAllArtists();

        // Assert
        verify(inMemoryDAO, times(1)).findAll();
    }

    @Test
    void saveArtist_shouldThrowExceptionIfArtistIsNull() {
        NullArtistException nullArtistException = assertThrows(NullArtistException.class, () -> artistDataService.saveArtist(null));
        String exceptionMessage = nullArtistException.getMessage();
        assertEquals("Artist cannot be null", exceptionMessage);
        verify(inMemoryDAO, times(0)).create(any());
        verify(inMemoryDAO, times(0)).update(any());
    }

    @Test
    void saveArtist_shouldCreateNewArtistWhenIdIsNull() {
        // Arrange
        Artist artist = Artist.builder().id(null).build();
        when(inMemoryDAO.create(artist)).thenReturn(artist);

        // Act
        Artist persistedArtist = artistDataService.saveArtist(artist);

        // Assert
        verify(inMemoryDAO, times(1)).create(persistedArtist);
        verify(inMemoryDAO, times(0)).update(any());
        assertNotNull(persistedArtist);
    }

    @Test
    void saveArtist_shouldUpdateArtistWhenIdIsNotNullAndIdExistsInDatastore() {
        // Arrange
        Artist artist = Artist.builder().id(1L).yearFounded(2025).build();
        assertNotNull(artist);
        when(inMemoryDAO.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(inMemoryDAO.update(artist)).thenReturn(artist);

        // Act
        Artist persistedArtist = artistDataService.saveArtist(artist);

        // Assert
        assertNotNull(persistedArtist);
        verify(inMemoryDAO, times(0)).create(any());
        verify(inMemoryDAO, times(1)).update(any());
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
        when(inMemoryDAO.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(inMemoryDAO.delete(artist.getId())).thenReturn(true);

        // Act
        boolean deleteArtistById = artistDataService.deleteArtistById(artist.getId());

        // Assert
        verify(inMemoryDAO, times(1)).findById(artist.getId());
        verify(inMemoryDAO, times(1)).delete(artist.getId());
        assertTrue(deleteArtistById);

    }

    @Test
    void deleteArtistById_shouldReturnFalseWhenNoArtistIsPresentToBeDeleted() {
        assertFalse(artistDataService.deleteArtistById(1L));
        verify(inMemoryDAO, times(1)).findById(anyLong());
        verify(inMemoryDAO, times(0)).delete(anyLong());
    }

    @Test
    void isExistingArtist_shouldReturnTrueIfArtistExists() {
        Artist artist = Artist.builder().id(1L).build();
        when(inMemoryDAO.findById(anyLong())).thenReturn(Optional.ofNullable(artist));
        assertTrue(artistDataService.isExistingArtist(artist));
    }

    @Test
    void isExistingArtist_shouldReturnFalseIfArtistDoesNotExist() {
        Artist artist = Artist.builder().id(1L).build();
        when(inMemoryDAO.findById(anyLong())).thenReturn(Optional.empty());
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