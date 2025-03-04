package com.pluralsight.EntertainmentManagementSpring.domain;

import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data

public abstract class Track extends BaseEntity {

    @EqualsAndHashCode.Include
    private String title;
    @EqualsAndHashCode.Include
    private int durationSeconds;
    @EqualsAndHashCode.Include
    private Genre genre;
    @Singular
    private List<Track> tracks;
    @EqualsAndHashCode.Include
    private int yearReleased;
    @EqualsAndHashCode.Include
    private int beatsPerMinute;
}
