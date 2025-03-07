package com.pluralsight.EntertainmentManagementSpring.controllers;

import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import com.pluralsight.EntertainmentManagementSpring.service.PlaylistService;
import com.pluralsight.EntertainmentManagementSpring.controllers.PlaylistController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlaylistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlaylistService playlistService;

    @InjectMocks
    private PlaylistController playlistController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Playlist playlist;
    private Track track;


    @BeforeEach
    void setUp() {
        playlist = new Playlist(List.of(), "Test Playlist", "A test playlist");
        playlist.setId(1L);
        track = new Track("Test Track", 200, null, List.of(), 2023, 120);
        track.setId(10L);
    }

    @Test
    void getAllPlaylists_ShouldReturnOk() throws Exception {
        when(playlistService.getAllPlaylists()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/playlists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(playlistService, times(1)).getAllPlaylists();
    }

    @Test
    void getPlaylistById_ShouldReturnPlaylist_WhenFound() throws Exception {
        when(playlistService.getPlaylistById(1L)).thenReturn(Optional.of(playlist));
        mockMvc.perform(get("/playlists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Rock Classics"));
        verify(playlistService, times(1)).getPlaylistById(1L);
    }

    @Test
    void getPlaylistById_ShouldReturnNotFound_WhenNotFound() throws Exception {
        when(playlistService.getPlaylistById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/playlists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        verify(playlistService, times(1)).getPlaylistById(1L);
    }

    @Test
    void createPlaylist_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/playlists/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playlist)))
                .andExpect(status().isCreated());
        verify(playlistService, times(1)).createPlaylist(any(Playlist.class));
    }

    @Test
    void editPlaylist_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(put("/playlists/1/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playlist)))
                .andExpect(status().isNoContent());
        verify(playlistService, times(1)).editPlaylist(eq(1L), any(Playlist.class));
    }

    @Test
    void deletePlaylist_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/playlists/1/delete"))
                .andExpect(status().isNoContent());
        verify(playlistService, times(1)).deletePlaylist(1L);
    }

    @Test
    void addTrackToPlaylist_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(put("/playlists/add/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(track)))
                .andExpect(status().isNoContent());
        verify(playlistService, times(1)).addTrackToPlaylist(eq(1L), any(Track.class));
    }

    @Test
    void removeTrackFromPlaylist_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(put("/playlists/remove/1/10"))
                .andExpect(status().isNoContent());
        verify(playlistService, times(1)).removeTrackFromPlaylist(1L, 10L);
    }

    @Test
    void getAllTracksFromPlaylist_ShouldReturnTracks() throws Exception {
        when(playlistService.getAllTracksFromPlaylist(1L))
                .thenReturn(Optional.of(List.of(track)));
        mockMvc.perform(get("/playlists/1/tracks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].title").value("Song Name"));
        verify(playlistService, times(1)).getAllTracksFromPlaylist(1L);
    }
}