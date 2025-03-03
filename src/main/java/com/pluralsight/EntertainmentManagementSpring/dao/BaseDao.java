package com.pluralsight.EntertainmentManagementSpring.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
    
    Optional <T> findById(Long id);
    List<T> findAll();
    T create(T t);
    boolean update(T t);
    boolean delete(Long id);
    void resetDatastore();

}
