package com.pluralsight.EntertainmentManagementSpring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pluralsight.EntertainmentManagementSpring.enums.ArtistType;
import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    @Singular
    private List<Genre> genres;

    private String biography;

    @EqualsAndHashCode.Include
    private String nationality;

    @EqualsAndHashCode.Include
    private int yearFounded;

    @JsonIgnore
    @ToString.Exclude
    @Singular
    private List<?> tracks;


}
