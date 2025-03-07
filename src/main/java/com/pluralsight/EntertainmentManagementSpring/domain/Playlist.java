package com.pluralsight.EntertainmentManagementSpring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Playlist extends BaseEntity{
    private List<Track> tracks;
    private String title;
    private String description;
}

