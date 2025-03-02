package com.example.backend.controllers.admin;

import com.example.backend.entities.Log;
import com.example.backend.services.LogService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/logs")
public class AdminLogController {

    private final LogService service;

    public AdminLogController(LogService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Log>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Log> getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @GetMapping("/by-user/{id}")
    public ResponseEntity<List<Log>> getLogsByUserId(@PathVariable("id") Integer id) {
        return service.getLogsByUserId(id);
    }
}