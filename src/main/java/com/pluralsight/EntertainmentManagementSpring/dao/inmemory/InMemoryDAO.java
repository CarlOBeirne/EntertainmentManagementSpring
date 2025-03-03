package com.pluralsight.EntertainmentManagementSpring.dao.inmemory;

import com.pluralsight.EntertainmentManagementSpring.dao.BaseDao;
import com.pluralsight.EntertainmentManagementSpring.domain.BaseEntity;

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
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(datastore.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(datastore.values());
    }

    @Override
    public T create(T entity) {
        entity.setId(idGenerator.getAndIncrement());
        return datastore.put(entity.getId(), entity);
    }

    @Override
    public T update(T entity) {
        return datastore.computeIfPresent(entity.getId(), (key, oldValue) -> entity);
    }

    @Override
    public boolean delete(Long id) {
        return datastore.remove(id) != null;
    }

    @Override
    public void resetDatastore() {
        datastore = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong(1);
    }
}
