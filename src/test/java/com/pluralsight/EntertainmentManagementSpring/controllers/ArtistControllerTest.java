package com.pluralsight.EntertainmentManagementSpring.controllers;

import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.service.ArtistDataService;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.InvalidArtistIdException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NullArtistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ArtistControllerTest {

    @Mock
    private ArtistDataService mockArtistDataService;

    @InjectMocks
    private ArtistController artistController;

    @Test
    void getArtistById_shouldThrowResponseStatusExceptionWithInternalServerError() {
        Mockito.when(mockArtistDataService.findArtistById(1L)).thenThrow(new RuntimeException("Thrown on purpose"));
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> artistController.getArtistById(1L));
        String exceptionMessage = responseStatusException.getMessage();
        assertEquals("500 INTERNAL_SERVER_ERROR \"Thrown on purpose\"", exceptionMessage);
    }

    @Test
    void getArtistById_shouldThrowInvalidArtistIdException() {
        Mockito.when(mockArtistDataService.findArtistById(1L)).thenThrow(new InvalidArtistIdException("Thrown on purpose"));
        InvalidArtistIdException invalidArtistIdException = assertThrows(InvalidArtistIdException.class, () -> artistController.getArtistById(1L));
        String exceptionMessage = invalidArtistIdException.getMessage();
        assertEquals("Thrown on purpose", exceptionMessage);
    }

    @Test
    void create_shouldThrowNullArtistException() {
        Artist artist = Artist.builder().build();
        Mockito.when(mockArtistDataService.saveArtist(artist)).thenThrow(new NullArtistException("Thrown on purpose"));
        NullArtistException nullArtistException = assertThrows(NullArtistException.class, () -> artistController.createArtist(artist));
        String exceptionMessage = nullArtistException.getMessage();
        assertEquals("Thrown on purpose", exceptionMessage);
    }

    @Test
    void create_shouldThrowResponseStatusExceptionWithInternalServerError() {
        Artist artist = Artist.builder().build();
        Mockito.when(mockArtistDataService.saveArtist(artist)).thenThrow(new RuntimeException("Thrown on purpose"));
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> artistController.createArtist(artist));
        String exceptionMessage = responseStatusException.getMessage();
        assertEquals("500 INTERNAL_SERVER_ERROR \"Thrown on purpose\"", exceptionMessage);
    }

    @Test
    void getAllArtists_shouldThrowResponseStatusExceptionWithInternalServerError() {
        Mockito.when(mockArtistDataService.findAllArtists()).thenThrow(new RuntimeException("Thrown on purpose"));
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> artistController.getAllArtists());
        String exceptionMessage = responseStatusException.getMessage();
        assertEquals("500 INTERNAL_SERVER_ERROR \"Thrown on purpose\"", exceptionMessage);
    }

    @Test
    void getArtistByName_shouldThrowResponseStatusExceptionWithInternalServerError() {
        Mockito.when(mockArtistDataService.findAllArtistsByName(Mockito.anyString())).thenThrow(new RuntimeException("Thrown on purpose"));
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> artistController.getArtistByName("Test"));
        String exceptionMessage = responseStatusException.getMessage();
        assertEquals("500 INTERNAL_SERVER_ERROR \"Thrown on purpose\"", exceptionMessage);
    }
}