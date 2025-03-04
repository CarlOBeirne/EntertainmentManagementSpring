package com.pluralsight.EntertainmentManagementSpring.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto {
    private Long id;
    private String name;
    private List<String> genres;
    private String biography;
    private String nationality;
    private int yearFounded;
    private List<String> tracks;

}
