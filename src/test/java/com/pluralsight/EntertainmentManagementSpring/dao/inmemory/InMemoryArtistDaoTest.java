package com.pluralsight.EntertainmentManagementSpring.dao.inmemory;

import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryArtistDaoTest {

    private InMemoryArtistDao artistDao = new InMemoryArtistDao();
//    private Artist artist1;
//    private Artist artist2;

    @BeforeEach
    void setUp() {
        artistDao = new InMemoryArtistDao();
        artistDao.resetDatastore();

//        artist1 = buildArtist("Artist 1", ArtistType.GROUP, "Artist 1 Bio", 2025);
//        artist2 = buildArtist("Artist 2", ArtistType.SOLO, "Artist 2 Bio", 2020);
    }

    @Test
    void create_shouldSetIdAndInsertArtistInDatastore() {
        // Arrange
        Artist artist = Artist.builder().build();
        Long expectedId = 1L;
        Long expectedNextId = 2L;
        assertNull(artist.getId());
        assertEquals(0, artistDao.datastore.size());

        // Act
        Artist persistedArtist = artistDao.create(artist);

        // Assert
        assertNotNull(persistedArtist);
        assertEquals(expectedId, persistedArtist.getId());
        assertEquals(expectedNextId, artistDao.idGenerator.get());

        assertEquals(1, artistDao.datastore.size());
        assertEquals(persistedArtist, artistDao.datastore.get(expectedId));

    }

    @Test
    void create_shouldThrowNullPointExceptionIfNullArtistIsPassed() {
        assertThrows(NullPointerException.class, () -> artistDao.create(null));
    }

    @Test
    void findById_shouldReturnArtist() {
        // Arrange
        Artist artist = artistDao.create(Artist.builder().build());

        // Act
        Optional<Artist> optionalArtist = artistDao.findById(artist.getId());

        // Assert
        assertTrue(optionalArtist.isPresent());
        assertEquals(artist, optionalArtist.get());
    }

    @Test
    void findAll_shouldReturnArtists() {
        assertTrue(artistDao.findAll().isEmpty());
        Artist artist1 = artistDao.create(Artist.builder().build());
        Artist artist2 = artistDao.create(Artist.builder().build());

        List<Artist> artistList = artistDao.findAll();

        assertEquals(2, artistList.size());
        assertEquals(artist1, artistList.get(0));
        assertEquals(artist2, artistList.get(1));
    }

    @Test
    void findById_shouldReturnEmptyOptionalIfArtistDoesNotExist() {
        // Act
        Optional<Artist> optionalArtist = artistDao.findById(1L);

        // Assert
        assertFalse(optionalArtist.isPresent());
    }

    @Test
    void delete_shouldDeleteArtist() {
        // Arrange
        Artist artist = artistDao.create(Artist.builder().build());

        // Act
        boolean isDeleted = artistDao.delete(artist.getId());

        // Assert
        assertTrue(isDeleted);
        Optional<Artist> optionalArtist = artistDao.findById(artist.getId());
        assertFalse(optionalArtist.isPresent());
    }

    @Test
    void delete_shouldReturnFalseIfArtistDoesNotExist() {
        boolean isDeleted = artistDao.delete(1L);
        assertFalse(isDeleted);
    }

    @Test
    void delete_shouldThrowExceptionIfNullIdIsPassed() {
        assertThrows(RuntimeException.class, () -> artistDao.delete(null));
    }

    @Test
    void update_shouldUpdateArtist() {
        // Arrange
        Artist artist = artistDao.create(Artist.builder().build());
        artist.setYearFounded(2015);
        Artist expectedArtist = Artist.builder().id(1L).yearFounded(2015).build();

        // Act
        Artist updatedArtist = artistDao.update(artist);

        // Assert
        assertEquals(expectedArtist, updatedArtist);
    }

    @Test()
    void update_shouldThrowExceptionIfNullArtistIsPassed() {
        assertThrows(RuntimeException.class, () -> artistDao.update(null));
    }

    @Test
    void update_shouldThrowExceptionIfArtistDoesNotExist() {
        // Arrange
        Artist artist = artistDao.create(Artist.builder().build());
        artistDao.delete(artist.getId());

        // Act
        Artist updatedArtist = artistDao.update(artist);

        // Assert
        assertNull(updatedArtist);
    }

//    private Artist buildArtist(String name, ArtistType type, String biography, int yearFounded) {
//        return Artist.builder()
//                .id(null)
//                .name(name)
//                .artistType(type)
//                .biography(biography)
//                .yearFounded(yearFounded)
//                .build();
//    }

}