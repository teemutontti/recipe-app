package com.example.backend.services;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User create(User user) {

        // Hashing password
        String hashedPassword = SecurityUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Encrypting personal information
        if (user.encrypt()) {
            return getRepository().save(user);
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> users = getRepository().findAll();
        List<User> decryptedUsers = new ArrayList<User>();

        // Decrypting user data
        users.forEach(user -> {
            if (user.decrypt()) {
                decryptedUsers.add(user);
            }
        });

        return decryptedUsers;
    }

    @Override
    public User getById(Long id) {
        User existingUser = getRepository().findById(id).orElse(null);
        if (existingUser != null && existingUser.decrypt()) {
            return existingUser;
        }
        return null;
    }

    @Override
    public User update(Long id, User user) {
        User existingUser = getRepository().findById(id).orElse(null);

        System.out.println("\n\n" + user + "\n\n");
        System.out.println("\n\n" + existingUser + "\n\n");

        if (existingUser != null && user.encrypt()) {
            // TODO: Implement update logic
            return getRepository().save(existingUser);
        }
        return null;
    }
}
