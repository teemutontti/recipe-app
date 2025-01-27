package com.example.backend.controllers;

import com.example.backend.services.LogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entities.Log;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    protected LogService service;

    @PostMapping
    public ResponseEntity<Log> create(@Valid @RequestBody Log entity) {
        return service.create(entity);
    }

    @GetMapping
    public ResponseEntity<List<Log>> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Log> getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Log> update(@PathVariable("id") Long id, @Valid @RequestBody Log item) {
        return service.update(id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Log> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<Log>> getLogsByDate(@RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getLogsByDate(parsedDate);
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<Log>> getLogsByUserId(@PathVariable("id") Integer id) {
        return service.getLogsByUserId(id);
    }
}