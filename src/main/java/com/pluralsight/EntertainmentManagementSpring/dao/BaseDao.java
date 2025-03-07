package com.pluralsight.EntertainmentManagementSpring.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface BaseDao<T> {
    
    Optional <T> findById(Long id);
    List<T> findAll();
    List<T> findByName(String name);
    T create(T t);
    T update(T t);
    boolean delete(Long id);
    void resetDatastore();
    default Optional<T> findBy(Predicate<T> predicate) {
        return findAll().stream()
                .filter(predicate)
                .findFirst();
    }
}
