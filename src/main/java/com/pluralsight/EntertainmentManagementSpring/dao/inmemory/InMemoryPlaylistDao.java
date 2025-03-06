package com.pluralsight.EntertainmentManagementSpring.dao.inmemory;

import com.pluralsight.EntertainmentManagementSpring.domain.Artist;
import com.pluralsight.EntertainmentManagementSpring.domain.BaseEntity;
import com.pluralsight.EntertainmentManagementSpring.domain.Playlist;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPlaylistDao extends InMemoryDAO<Playlist> {
}
