package com.example.backend.controllers.admin;

import com.example.backend.dto.UserDto;
import com.example.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entities.User;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService service;

    public AdminUserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        return service.getAll(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @Valid @RequestBody UserDto item) {
        return service.update(id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }
}