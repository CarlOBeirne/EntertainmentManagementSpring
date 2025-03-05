package com.pluralsight.EntertainmentManagementSpring.service;

import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryDAO;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.utils.events.ArtistDeletionEvent;
import com.pluralsight.EntertainmentManagementSpring.utils.events.TrackCreatedEvent;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.InvalidArtistIdException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NoArtistFoundException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NullArtistException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArtistDataService {

    private final InMemoryDAO<Artist> artistDao;
    private final InMemoryDAO<Track> trackDao;

    private final ApplicationEventPublisher artistEventPublisher;

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
            Artist artist = optionalArtist.get();
            List<Long> trackIds = new ArrayList<>();
            for (Track track : artist.getTracks()) {
                trackIds.add(track.getId());
            }
            artistEventPublisher.publishEvent(new ArtistDeletionEvent(this, artist.getId(), trackIds));
            return artistDao.delete(id);
        }
        return false;
    }

    public List<Artist> findAllArtistsByName(@NonNull String name) {
        return findAllArtists().stream()
                .filter(artist -> artist.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @EventListener
    public void handleTrackCreatedEvent(@NonNull TrackCreatedEvent trackCreatedEvent) {
        Optional<Track> optionalTrack = trackDao.findById(trackCreatedEvent.getTrackId());
        if (optionalTrack.isPresent()) {
            ArrayList<Artist> artistList = new ArrayList<>();
            trackCreatedEvent.getArtistIds()
                    .forEach(artistId -> artistDao.findById(artistId)
                            .ifPresent(artistList::add));
            for (Artist artist : artistList) {
                artist.addTrack(optionalTrack.get());
                artist.addGenre(optionalTrack.get().getGenre());
            }
        }
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
