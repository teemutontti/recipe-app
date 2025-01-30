package com.example.backend.controllers;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.UserDto;
import com.example.backend.entities.User;
import com.example.backend.services.UserService;
import com.example.backend.utils.JwtTokenUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthenticationController(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> signIn(@Valid @RequestBody LoginRequest loginRequest) throws JOSEException {
        ResponseEntity<Boolean> response = userService.isEmailTaken(loginRequest.getEmail());
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {

            UserDto newUser = new UserDto(null, loginRequest.getEmail(), loginRequest.getPassword());
            ResponseEntity<User> created = userService.create(newUser);

            if (created != null) {
                String token = jwtTokenUtil.generateToken(loginRequest.getEmail());
                return new ResponseEntity<>(token, HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) throws JOSEException {
        ResponseEntity<Boolean> response = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (response.getStatusCode() == HttpStatus.OK) {
            String token = jwtTokenUtil.generateToken(loginRequest.getEmail());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
