package com.pluralsight.EntertainmentManagementSpring.dao;

import java.util.List;

public interface BaseDao<T> {
    
    T findById(Long id);
    List<T> findAll();
    T create(T t);
    T update(T t);
    boolean delete(Long id);
    void resetDatastore();

}
