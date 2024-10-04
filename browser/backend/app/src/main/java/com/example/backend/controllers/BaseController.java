package com.example.backend.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.backend.entities.BaseEntity;
import com.example.backend.services.BaseService;

/**
 * This class is a template for creating controllers.
 * It provides basic CRUD operations for the entity type T.
 * @param <T> Entity type to be used in the controller.
 */
class BaseController<T extends BaseEntity<T>> {

    @Autowired
    private BaseService<T> service;

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        return service.create(entity);
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable("id") Long id, @RequestBody T food) {
        return service.update(id, food);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<T> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }
}
