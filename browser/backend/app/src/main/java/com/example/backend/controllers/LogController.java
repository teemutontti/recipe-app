package com.example.backend.controllers;

import com.example.backend.services.LogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entities.Log;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    protected final LogService service;

    public LogController(LogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Log> create(@Valid @RequestBody Log entity) {
        return service.create(entity);
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
    public ResponseEntity<List<Log>> getLogsByDateAndUser(@RequestParam String date, @RequestParam Integer userId) {
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getLogsByDateAndUser(parsedDate, userId);
    }
}