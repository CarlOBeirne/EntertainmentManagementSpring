package com.pluralsight.EntertainmentManagementSpring.controllers;

import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
import com.pluralsight.EntertainmentManagementSpring.models.ArtistDto;
import com.pluralsight.EntertainmentManagementSpring.service.ArtistDataService;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.InvalidArtistIdException;
import com.pluralsight.EntertainmentManagementSpring.utils.exceptions.NullArtistException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
@Log4j2
public class ArtistController {

    private final ArtistDataService artistDataService;

    @PostMapping(path = "/new")
    public ResponseEntity<ArtistDto> createArtist(@RequestBody Artist artist)  {
        try {
            if (artist.getId() != null) {
                log.warn("Artist was passed with an Id of {} (expecting Id to be null)", artist.getId());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            artistDataService.saveArtist(artist);
            ArtistDto artistResponse = buildArtistToDto(artist);
            return ResponseEntity.ok(artistResponse);
        } catch (NullArtistException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ArtistDto> getArtistById(@NonNull @RequestParam Long artistId)  {
        try {
            Optional<Artist> optionalArtist = artistDataService.findArtistById(artistId);
            return optionalArtist
                    .map(artist -> {
                        ArtistDto artistDto = buildArtistToDto(artist);
                        return ResponseEntity.ok(artistDto);
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (InvalidArtistIdException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ArtistDto>> getAllArtists() {
        try {
            List<Artist> allArtists = artistDataService.findAllArtists();
            if (allArtists.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            ArrayList<ArtistDto> artistDtoList = new ArrayList<>();
            allArtists.forEach(artist -> artistDtoList.add(buildArtistToDto(artist)));
            return ResponseEntity.ok(artistDtoList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(path = "/name/{name}")
    public ResponseEntity<List<ArtistDto>> getArtistByName(@PathVariable String name) {
        if (name.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        try {
            List<Artist> allArtistsByName = artistDataService.findAllArtistsByName(name);
            if (allArtistsByName.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            ArrayList<ArtistDto> artistDtoList = new ArrayList<>();
            allArtistsByName.forEach(artist -> artistDtoList.add(buildArtistToDto(artist)));
            return ResponseEntity.ok(artistDtoList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> deleteArtistById(@NonNull @PathVariable Long id)  {
        try {
            Optional<Artist> optionalArtist = artistDataService.findArtistById(id);
            if (optionalArtist.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(artistDataService.deleteArtistById(id));
        } catch (InvalidArtistIdException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private static ArtistDto buildArtistToDto(Artist artist) {
        return ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .genres(artist.getGenres() == null ? null : artist.getGenres().stream()
                        .map(Genre::name)
                        .toList())
                .biography(artist.getBiography())
                .nationality(artist.getNationality())
                .yearFounded(artist.getYearFounded())
                .tracks(artist.getTracks() == null ? null : artist.getTracks().stream()
                        .map(Track::getTitle)
                        .toList()).build();
    }
}
