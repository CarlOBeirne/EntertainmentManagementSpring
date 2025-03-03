package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.enums.ArtistType;
import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
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
        when(inMemoryDAO.findById(anyLong())).thenReturn(Optional.ofNullable(getArtists().get(1)));

        // Act
        Optional<Artist> optionalArtist = artistDataService.findArtistById(1L);

        // Assert
        assertNotNull(optionalArtist);
        assertTrue(optionalArtist.isPresent());
        assertEquals(getArtists().get(1), optionalArtist.get());
    }

    @Test
    void findArtistById_shouldReturnEmptyOptionalIfArtistDoesNotExist() {
        // Arrange
        when(inMemoryDAO.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Artist> optionalArtist = artistDataService.findArtistById(1L);

        // Assert
        assertTrue(optionalArtist.isEmpty());

    }

    @Test
    void findArtistById_shouldThrowExceptionIfNullIdIsPassed() {
        InvalidArtistIdException invalidArtistIdException = assertThrows(InvalidArtistIdException.class, () -> artistDataService.findArtistById(null));
        assertNotNull(invalidArtistIdException);
        String exceptionMessage = invalidArtistIdException.getMessage();
        assertEquals("Artist id cannot be null", exceptionMessage);
        verify(inMemoryDAO, times(0)).findById(anyLong());

    }

    @Test
    void findAllArtists_shouldReturnAllArtists() {
        // Arrange
        when(inMemoryDAO.findAll()).thenReturn(getArtists());

        // Act
        List<Artist> allArtists = artistDataService.findAllArtists();

        // Assert
        verify(inMemoryDAO, times(1)).findAll();
        assertNotNull(allArtists);
        assertFalse(allArtists.isEmpty());
        allArtists.forEach(artist -> assertInstanceOf(Artist.class, artist));
    }

    @Test
    void saveArtist_shouldThrowExceptionIfArtistIsNull() {
        NullArtistException nullArtistException = assertThrows(NullArtistException.class, () -> artistDataService.saveArtist(null));
        assertNotNull(nullArtistException);
        String exceptionMessage = nullArtistException.getMessage();
        assertEquals("Artist cannot be null", exceptionMessage);
        verify(inMemoryDAO, times(0)).create(any());
    }

    @Test
    void saveArtist_shouldCreateNewArtistWhenIdIsNull() {
        // Arrange
        Artist actualArtist = getArtists().getFirst();
        when(inMemoryDAO.create(actualArtist)).thenReturn(actualArtist);
        assertNull(actualArtist.getId());

        // Act
        Artist artist = artistDataService.saveArtist(actualArtist);

        // Assert
        verify(inMemoryDAO, times(1)).create(any());
        assertNotNull(artist);
        assertEquals(actualArtist, artist);
    }

    @Test
    void saveArtist_shouldUpdateArtistWhenIdIsNotNullAndIdExistsInDatastore() {
        // Arrange
        Artist actualArtist = getArtists().get(1);
        when(inMemoryDAO.findById(anyLong())).thenReturn(Optional.ofNullable(actualArtist));
        assertNotNull(actualArtist);
        Artist updatedArtist = Artist.builder()
                .id(actualArtist.getId())
                .name(actualArtist.getName())
                .artistType(actualArtist.getArtistType())
                .genres(actualArtist.getGenres())
                .yearFounded(2025)
                .build();
        when(inMemoryDAO.update(any())).thenReturn(updatedArtist);

        // Act
        Artist persistedArtist = artistDataService.saveArtist(updatedArtist);

        // Assert
        verify(inMemoryDAO, times(0)).create(any());
        verify(inMemoryDAO, times(1)).update(any());
        assertEquals(updatedArtist, persistedArtist);
        assertEquals(2025, persistedArtist.getYearFounded());
    }

    @Test
    void saveArtist_shouldThrowExceptionIfArtistIdIsPresentButNotInDatastore() {
        Artist actualArtist = getArtists().get(1);
        NoArtistFoundException noArtistFoundException = assertThrows(NoArtistFoundException.class, () -> artistDataService.saveArtist(actualArtist));
        assertNotNull(noArtistFoundException);
        String exceptionMessage = noArtistFoundException.getMessage();
        assertTrue(exceptionMessage.startsWith("No artist found with id "));
    }

    @Test
    void deleteArtistById_shouldThrowExceptionIfArtistIdIsNull() {
        InvalidArtistIdException invalidArtistIdException = assertThrows(InvalidArtistIdException.class, () -> artistDataService.deleteArtistById(null));
        assertNotNull(invalidArtistIdException);
        String exceptionMessage = invalidArtistIdException.getMessage();
        assertTrue(exceptionMessage.startsWith("Artist id cannot be null"));
    }

    @Test
    void deleteArtistById_shouldDeleteArtistAndReturnTrueWhenIdIsNotNullAndIdExistsInDatastore() {
        // Arrange
        Artist artist = getArtists().get(1);
        when(inMemoryDAO.findById(anyLong())).thenReturn(Optional.ofNullable(artist));
        when(inMemoryDAO.delete(anyLong())).thenReturn(true);
        assertNotNull(artist);

        // Act
        boolean deleteArtistById = artistDataService.deleteArtistById(artist.getId());

        // Assert
        verify(inMemoryDAO, times(1)).findById(anyLong());
        verify(inMemoryDAO, times(1)).delete(anyLong());
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
        Artist artistInDatastore = getArtists().get(1);
        when(inMemoryDAO.findById(anyLong())).thenReturn(Optional.ofNullable(artistInDatastore));

        assertTrue(artistDataService.isExistingArtist(artistInDatastore));
    }

    @Test
    void isExistingArtist_shouldReturnFalseIfArtistDoesNotExist() {
        Artist artistNotInDatastore = getArtists().getLast();
        when(inMemoryDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertFalse(artistDataService.isExistingArtist(artistNotInDatastore));
    }

    @Test
    void validArtistObjectCheck_shouldThrowExceptionIfArtistIsNull() {
        NullArtistException nullArtistException = assertThrows(NullArtistException.class, () -> ArtistDataService.validArtistObjectCheck(null));
        assertNotNull(nullArtistException);
        String exceptionMessage = nullArtistException.getMessage();
        assertEquals("Artist cannot be null", exceptionMessage);
    }

    @Test
    void validArtistObjectCheck_shouldNotThrowExceptionIfArtistIsPresent() {
        Artist artist = getArtists().get(1);
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
        assertDoesNotThrow(() -> ArtistDataService.nullIdCheck(1L));
    }

    private List<Artist> getArtists() {
        Artist artist1 = Artist.builder()
                .id(null)
                .name("Artist 1")
                .artistType(ArtistType.SOLO)
                .genre(Genre.COUNTRY)
                .genre(Genre.FOLK)
                .biography("Artist 1 Bio")
                .build();
        Artist artist2 = Artist.builder()
                .id(1L)
                .name("Artist 2")
                .artistType(ArtistType.GROUP)
                .genre(Genre.POP)
                .biography("Artist 2 Bio")
                .build();
        Artist artist3 = Artist.builder()
                .id(2L)
                .name("Artist 3")
                .artistType(ArtistType.SOLO)
                .genre(Genre.HEAVY_METAL)
                .biography("Artist 3 Bio")
                .build();
        return List.of(artist1, artist2, artist3);
    }
}