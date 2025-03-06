package com.pluralsight.EntertainmentManagementSpring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pluralsight.EntertainmentManagementSpring.enums.ArtistType;
import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=true)
public class Artist extends BaseEntity {

    @EqualsAndHashCode.Include
    private String name;

    @EqualsAndHashCode.Include
    private ArtistType artistType;

    @JsonIgnore
    @Singular
    private List<Genre> genres = new ArrayList<>();

    private String biography;

    @EqualsAndHashCode.Include
    private String nationality;

    @EqualsAndHashCode.Include
    private int yearFounded;

    @JsonIgnore
    @ToString.Exclude
    @Singular
    private List<Track> tracks = new ArrayList<>();

    public void addGenre(@NonNull Genre genre) {
        if (!genres.contains(genre)) {
            genres.add(genre);
        }
    }

    public void addTrack(@NonNull Track track) {
        Optional<Track> optionalTrack = tracks.stream()
                .filter(t -> t.getTitle().equalsIgnoreCase(track.getTitle()))
                .findFirst();
        if (optionalTrack.isEmpty()) {
            tracks.add(track);
        }
    }


}
