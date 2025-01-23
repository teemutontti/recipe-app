package com.example.backend.controllers;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.EncryptionKeyException;
import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.services.UserService;
import com.example.backend.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entities.User;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    protected UserService service;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserDto entity) {
        return service.create(entity);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @Valid @RequestBody UserDto item) {
        System.out.println("IN CONTROLLER");
        return service.update(id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) throws EncryptionKeyException, FailedCryptionException {
        User user = service.login(loginRequest.getId(), loginRequest.getPassword()).getBody();

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        user.setEmail(SecurityUtil.decrypt(user.getEmail()));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}