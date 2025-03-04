package com.pluralsight.EntertainmentManagementSpring.dao.inmemory;

import com.pluralsight.EntertainmentManagementSpring.domain.BaseEntity;
import com.pluralsight.EntertainmentManagementSpring.domain.Track;
import org.springframework.stereotype.Repository;

//extends <Track>?
@Repository
public class InMemoryTrackDAO<T extends BaseEntity> extends InMemoryDAO<T>{
}

