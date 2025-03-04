package com.pluralsight.EntertainmentManagementSpring.dao.inmemory;

import com.pluralsight.EntertainmentManagementSpring.dao.BaseDao;
import com.pluralsight.EntertainmentManagementSpring.domain.BaseEntity;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class InMemoryDAO<T extends BaseEntity> implements BaseDao<T> {
    protected Map<Long, T> datastore = new ConcurrentHashMap<>();
    protected AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Optional<T> findById(@NonNull Long id) {
        return Optional.ofNullable(datastore.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(datastore.values());
    }

    @Override
    public T create(@NonNull T entity) {
        entity.setId(idGenerator.getAndIncrement());
        datastore.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public T update(@NonNull T entity) {
        return datastore.computeIfPresent(entity.getId(), (key, oldValue) -> entity);
    }

    @Override
    public boolean delete(@NonNull Long id) {
        return datastore.remove(id) != null;
    }

    @Override
    public void resetDatastore() {
        datastore = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong(1);
    }
}
