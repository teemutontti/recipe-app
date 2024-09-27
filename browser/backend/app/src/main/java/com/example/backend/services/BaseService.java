package com.example.backend.services;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.entities.BaseEntity;

/**
 * BaseService
 * This class is a generic service that provides basic create, read, and delete
 * operations for an entity type T.
 * @param <T> Entity type to be used in the service.
 */
public abstract class BaseService<T extends BaseEntity<T>> {

    protected abstract JpaRepository<T, Long> getRepository();

    public T create(T entity) {
        System.out.println("\n\n" + entity + "\n\n");
        return getRepository().save(entity);
    }

    public List<T> getAll() {
        return getRepository().findAll();
    }

    public T getById(Long id) {
        return getRepository().findById(id).orElse(null);
    }

    public T update(Long id, T entity) {
        T existingEntity = getRepository().findById(id).orElse(null);

        if (existingEntity != null) {
            existingEntity.update(entity);
            return getRepository().save(existingEntity);
        }
        return null;
    }

    public void delete(Long id) {
        getRepository().deleteById(id);
    }
}