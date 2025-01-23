package com.example.backend.services;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.exceptions.EncryptionKeyException;
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
public class UserService extends BaseService<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected UserRepository getRepository() {
        return userRepository;
    }

    @Override
    public ResponseEntity<User> create(User user) {

        // Hashing password
        String hashedPassword = SecurityUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        try {
            // Encrypting personal information
            User encryptedUser = SecurityUtil.encryptUser(user);

            User data = getRepository().save(encryptedUser);
            return new ResponseEntity<>(data, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println(e);
            System.err.println("User encryption failed!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<User>> getAll() {
        try {
            List<User> users = (List<User>) getRepository().findAll();
            List<User> decryptedUsers = new ArrayList<>();

            // Decrypting user data
            for (User user: users) {
                try {
                    User decryptedUser = SecurityUtil.decryptUser(user);
                    decryptedUsers.add(decryptedUser);
                } catch (FailedDecryptionException e) {
                    System.err.println("Decryption failed for user " + user.getId());
                }
            }
            return new ResponseEntity<>(decryptedUsers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<User> getById(Long id) {
        try {
            User existingUser = getRepository().findById(id).orElse(null);
            if (existingUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            try {
                User decryptedUser = SecurityUtil.decryptUser(existingUser);
                return new ResponseEntity<>(decryptedUser, HttpStatus.OK);
            } catch (FailedDecryptionException e) {
                System.err.print("Decryption failed for user " + existingUser.getId());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<User> update(Long id, User user) {
        try {
            User existingUser = getRepository().findById(id).orElse(null);
            if (existingUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            try {
                User encryptedUser = SecurityUtil.encryptUser(user);
                existingUser.update(encryptedUser);

                User data = getRepository().save(existingUser);
                return new ResponseEntity<>(data, HttpStatus.OK);
            } catch (FailedEncryptionException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
