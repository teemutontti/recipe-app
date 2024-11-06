package com.example.backend.controllers;
import com.example.backend.dto.LoginRequest;
import com.example.backend.exceptions.FailedDecryptionException;
import com.example.backend.services.UserService;
import com.example.backend.utils.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entities.User;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User, UserService> {
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) throws FailedDecryptionException {
        ResponseEntity<User> response = service.login(loginRequest.getId(), loginRequest.getPassword());
        User user = response.getBody();

        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(SecurityUtil.decryptUser(user), HttpStatus.OK);
    }

}