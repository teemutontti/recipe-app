package com.example.backend.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    public T create(@RequestBody T entity) {
        return service.create(entity);
    }

    @GetMapping
    public List<T> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public T getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public T update(@PathVariable Long id, @RequestBody T food) {
        return service.update(id, food);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
