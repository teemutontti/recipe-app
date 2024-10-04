package com.example.backend.services;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.backend.entities.BaseEntity;

/**
 * BaseService
 * This class is a generic service that provides basic create, read, and delete
 * operations for an entity type T.
 * @param <T> Entity type to be used in the service.
 */
public abstract class BaseService<T extends BaseEntity<T>> {

    protected abstract JpaRepository<T, Long> getRepository();

    public ResponseEntity<T> create(T entity) {
        try {
            T data = getRepository().save(entity);
            return new ResponseEntity<>(data, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<T>> getAll() {
        try {
            List<T> data = (List<T>) getRepository().findAll();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<T> getById(Long id) {
        try {
            T data = getRepository().findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<T> update(Long id, T entity) {
        try {
            T existingEntity = getRepository().findById(id).orElse(null);

            if (existingEntity != null) {
                existingEntity.update(entity);
                T data = getRepository().save(existingEntity);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<T> delete(Long id) {
        try {
            getRepository().deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}