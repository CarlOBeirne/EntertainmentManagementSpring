package com.pluralsight.EntertainmentManagementSpring.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.enums.Genre;
import com.pluralsight.EntertainmentManagementSpring.service.TrackDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.http.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")

class TrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrackDataService trackDataService;

    @Autowired
    private ObjectMapper objectMapper;

    private Track testTrack;

    @BeforeEach
    public void setUp() {
        testTrack = Track.builder()
                //.id(1L)
                .title("Roxanne")
                .durationSeconds(120)
                .genre(Genre.ROCK)
                .yearReleased(1980)
                .beatsPerMinute(120)
                .build();
    }


    @Test
    public void testGetAllTracks() throws Exception {
        //Arrange
        when(trackDataService.getAllTracks()).thenReturn(List.of(testTrack));

        //Act, Assert
        mockMvc.perform(get("/tracks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllTracks_NoContent() throws Exception {
        when(trackDataService.getAllTracks()).thenReturn(List.of());

        mockMvc.perform(get("/tracks"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetTrackById() throws Exception {
        when(trackDataService.getByTrackId(1L)).thenReturn(testTrack);

        mockMvc.perform(get("/tracks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateTrack() throws Exception {
        when(trackDataService.createTrack(any(Track.class))).thenReturn(testTrack);

        mockMvc.perform(MockMvcRequestBuilders.post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTrack)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(testTrack.getTitle()));

    }

    @Test
    public void testUpdateTrack() throws Exception {
        Track testTrack = Track.builder()
                .id(1L)
                .title("Roxanne")
                .durationSeconds(120)
                .genre(Genre.ROCK)
                .yearReleased(1980)
                .beatsPerMinute(120)
                .build();

        Track updatedTrack = testTrack.toBuilder()
                .title("UpdatedSongTitle")
                .build();

        Mockito.when(trackDataService.createTrack(Mockito.any(Track.class))).thenReturn(testTrack);
        Mockito.when(trackDataService.updateTrack(Mockito.any(Track.class))).thenReturn(updatedTrack);

        mockMvc.perform(MockMvcRequestBuilders.put("/tracks/" + testTrack.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTrack)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("UpdatedSongTitle"));

        Mockito.verify(trackDataService, Mockito.times(1)).updateTrack(Mockito.any(Track.class));
    }


    @Test
    public void testDeleteTrack() throws Exception {
        Track testTrack = Track.builder()
                .id(1L)
                .title("Roxanne")
                .durationSeconds(120)
                .genre(Genre.ROCK)
                .yearReleased(1980)
                .beatsPerMinute(120)
                .build();

        Mockito.when(trackDataService.createTrack(Mockito.any(Track.class))).thenReturn(testTrack);
        Mockito.when(trackDataService.deleteTrack(1L)).thenReturn(true);

        mockMvc.perform(delete("/tracks/" + testTrack.getId()))
                .andExpect(status().isAccepted());
    }
}
