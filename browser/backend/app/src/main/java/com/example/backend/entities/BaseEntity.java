package com.example.backend.entities;

public interface BaseEntity<T> {
    boolean update(T entity);
}
