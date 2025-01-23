package com.example.backend.services;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.FailedDecryptionException;
import com.example.backend.exceptions.FailedEncryptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.utils.SecurityUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public ResponseEntity<User> create(UserDto userDto) {
        try {
            System.out.println("IN SERVICE");
            User user = new User(
                null,
                SecurityUtil.encrypt(userDto.getEmail()),
                SecurityUtil.hashPassword(userDto.getPassword())
            );
            System.out.println("Before save: " + user);

            User data = repository.save(user);

            System.out.println("After save: " + data);

            data.setEmail(SecurityUtil.decrypt(data.getEmail()));

            System.out.println("data");

            data.setPassword(null);

            return new ResponseEntity<>(data, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<User>> getAll() {
        try {
            List<User> users = repository.findAll();
            List<User> decryptedUsers = new ArrayList<>();

            // Decrypting user data
            for (User user: users) {
                System.out.println(user);
                try {
                    user.setEmail(SecurityUtil.decrypt(user.getEmail()));
                    user.setPassword(null);

                    decryptedUsers.add(user);
                } catch (FailedDecryptionException e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>(decryptedUsers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> getById(Long id) {
        try {
            User user = repository.findById(id).orElse(null);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            try {
                user.setEmail(SecurityUtil.decrypt(user.getEmail()));
                user.setPassword(null);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } catch (FailedDecryptionException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> update(Long id, UserDto userDto) {
        try {
            System.out.println("IN SERVICE");
            User existingUser = repository.findById(id).orElse(null);
            if (existingUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


            try {
                userDto.setEmail(SecurityUtil.encrypt(userDto.getEmail()));
                User data = repository.save(userDto.toUser());
                System.out.println(data);
                data.setPassword(null);
                return new ResponseEntity<>(data, HttpStatus.OK);
            } catch (FailedEncryptionException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<User> login(Long id, String password) {
        User existingUser = repository.findById(id).orElse(null);

        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (SecurityUtil.checkPassword(password, existingUser.getPassword())) {
            existingUser.setPassword(null);

            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<User> delete(Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
