package com.example.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.backend.entities.Log;
import com.example.backend.repositories.LogRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository repository;

    public ResponseEntity<Log> create(Log entity) {
        try {
            Log data = repository.save(entity);
            return new ResponseEntity<>(data, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Log>> getAll() {
        try {
            List<Log> data = repository.findAll();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Log> getById(Long id) {
        try {
            Log data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Log> update(Long id, Log entity) {
        try {
            Log existingEntity = repository.findById(id).orElse(null);

            if (existingEntity != null) {
                Log data = repository.save(entity);
                return new ResponseEntity<>(data, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Log> delete(Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Log>> getLogsByDate(LocalDate date) {
        try {
            List<Log> logs = repository.findByDate(date);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}