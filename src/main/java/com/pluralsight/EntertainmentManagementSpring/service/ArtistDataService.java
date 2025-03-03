package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.InvalidArtistIdException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NoArtistFoundException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NullArtistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArtistDataService {

    private final InMemoryDAO<Artist> artistDao;

    public Optional<Artist> findArtistById(Long id) {
        nullIdCheck(id);
        return artistDao.findById(id);
    }

    public List<Artist> findAllArtists() {
        return artistDao.findAll();
    }

    public Artist saveArtist(Artist artist) {
        validArtistObjectCheck(artist);
        if (artist.getId() == null) {
            return artistDao.create(artist);
        } else if (isExistingArtist(artist)) {
            return artistDao.update(artist);
        } else {
            throw new NoArtistFoundException("No artist found with id " + artist.getId());
        }
    }

    public boolean deleteArtistById(Long id) {
        nullIdCheck(id);
        Optional<Artist> optionalArtist = artistDao.findById(id);
        if (optionalArtist.isPresent()) {
            return artistDao.delete(id);
        }
        return false;
    }

    protected boolean isExistingArtist(Artist artist) {
        validArtistObjectCheck(artist);
        Optional<Artist> optionalArtist = artistDao.findById(artist.getId());
        return optionalArtist.isPresent();
    }

    protected static void validArtistObjectCheck(Artist artist) {
        if (artist == null) {
            throw new NullArtistException("Artist cannot be null");
        }
    }

    protected static void nullIdCheck(Long id) {
        if (id == null) {
            throw new InvalidArtistIdException("Artist id cannot be null");
        }
    }
}
