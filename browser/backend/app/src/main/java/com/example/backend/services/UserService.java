package com.example.backend.services;
import java.util.ArrayList;
import java.util.List;
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

        // Encrypting personal information
        boolean encryptionSuccess = user.encrypt();

        if (encryptionSuccess) {
            try {
                User data = getRepository().save(user);
                return new ResponseEntity<>(data, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<User>> getAll() {
        try {
            List<User> users = (List<User>) getRepository().findAll();
            List<User> decryptedUsers = new ArrayList<User>();

            // Decrypting user data
            for (User user: users) {
                boolean decryptionSuccess = user.decrypt();
                if (decryptionSuccess) {
                    decryptedUsers.add(user);
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

            boolean decryptionSuccess = existingUser.decrypt();
            if (decryptionSuccess) {
                return new ResponseEntity<>(existingUser, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<User> update(Long id, User user) {
        try {
            User existingUser = getRepository().findById(id).orElse(null);
            if (existingUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            boolean encryptionSuccess = user.encrypt();
            if (encryptionSuccess) {
                existingUser.update(user);
                try {
                    User data = getRepository().save(existingUser);
                    return new ResponseEntity<>(data, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
