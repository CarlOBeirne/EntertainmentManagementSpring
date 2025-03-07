package com.pluralsight.EntertainmentManagementSpring.dao.inmemory;
import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryPlaylistDao {
    private final Map<String, Playlist> playlistStorage = new ConcurrentHashMap<>();
    protected AtomicLong idGenerator = new AtomicLong(1);

    public List<Playlist> findAllPlaylist() {
        return new ArrayList<>(playlistStorage.values());
    }

    public List<Playlist> findPlaylistByName(String title) {
        return playlistStorage  .values()
                                .stream()
                                .filter(p -> p.getTitle().toLowerCase().contains(title.toLowerCase()))
                                .collect(Collectors.toList());
    }

    public Optional<Playlist> findPlaylistById(Long id) {
        return Optional.ofNullable(playlistStorage.get(id.toString()));
    }

    public Optional<List<Track>> findAllPlaylistTracks(Long playlistId) {
        return findPlaylistById(playlistId).map(Playlist::getTracks);
    }

    public Optional<List<Track>> findPlaylistTrack(Long playlistId, String trackName) {
        return findPlaylistById(playlistId).map(playlist ->
                playlist.getTracks().stream()
                        .filter(track -> track.getTitle().toLowerCase().contains(trackName.toLowerCase()))
                        .collect(Collectors.toList()));
    }

    public Optional<Track> findPlaylistTrackById(Long playlistId, Long trackId) {
        return findPlaylistById(playlistId)
                .flatMap(playlist -> playlist.getTracks().stream()
                        .filter(track -> track.getId().equals(trackId))
                        .findFirst());
    }

    public void save(Playlist playlist) {
        if (playlist.getId() == null || playlist.getId().describeConstable().isEmpty()){
            playlist.setId(idGenerator.getAndIncrement());
        }
        playlistStorage.put(playlist.getId().toString(), playlist);
    }

    public void editPlaylist(Long id, Playlist updatedPlaylist) {
        findPlaylistById(id).ifPresent(existingPlaylist -> {
            existingPlaylist.setTitle(updatedPlaylist.getTitle());
            existingPlaylist.setDescription(updatedPlaylist.getDescription());
        });
    }

    public void deletePlaylist(Long id) {
        playlistStorage.remove(id.toString());
    }

    public void addTrackToPlaylist(Long playlistId, Track track) {
        findPlaylistById(playlistId).map(playlist -> playlist.getTracks().add(track));
    }

    public void removeTrackFromPlaylist(Long playlistId, Long trackId) {
        findPlaylistById(playlistId).map(playlist ->
                playlist.getTracks().removeIf(track -> track.getId().equals(trackId)));
    }
}
