package com.pluralsight.EntertainmentManagementSpring.domain;

import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data

public  class Track extends BaseEntity {

    @EqualsAndHashCode.Include
    private String title;
    @EqualsAndHashCode.Include
    private int durationSeconds;
    @EqualsAndHashCode.Include
    private Genre genre;
    @Singular
    private List<Artist> artists = new ArrayList<>();
    @EqualsAndHashCode.Include
    private int yearReleased;
    @EqualsAndHashCode.Include
    private int beatsPerMinute;

    public void removeArtist(Artist artist) {
            artists.removeIf(a -> Objects.equals(a.getId(), artist.getId()));
    }
}
