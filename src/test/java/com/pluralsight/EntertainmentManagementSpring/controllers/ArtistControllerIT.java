package com.pluralsight.EntertainmentManagementSpring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pluralsight.EntertainmentManagementSpring.dao.inmemory.InMemoryArtistDao;
import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.service.ArtistDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ArtistControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArtistDataService artistDataService;

    @Autowired
    private InMemoryArtistDao inMemoryArtistDao;

    @BeforeEach
    void setUp() {
        inMemoryArtistDao.resetDatastore();
    }

    @Test
    void getArtistById_shouldReturnHttpStatusOk() throws Exception {
        Artist artist = Artist.builder().build();
        artistDataService.saveArtist(artist);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artist?artistId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void getArtistById_shouldReturnHttpStatusNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artist?artistId=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturnHttpsStatusConflict() throws Exception {
        Artist artist = Artist.builder().id(1L).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/artist/new")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(artist)))
                .andExpect(status().isConflict());
    }

    @Test
    void create_shouldReturnHttpStatusOk() throws Exception {
        Artist artist = Artist.builder().build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/artist/new")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(artist)))
                .andExpect(status().isOk());
    }
}