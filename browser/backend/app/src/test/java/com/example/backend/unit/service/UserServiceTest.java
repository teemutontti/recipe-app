package com.example.backend.unit.service;

import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserService;
import com.example.backend.utils.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Test
    public void testAddUser() {
        User user = new User(1, "test@gmail.com", "password");
        when(repository.save(user)).thenReturn(user);

        ResponseEntity<User> response = service.create(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().getId());

        // NOTE: Email should be encrypted and password hashed!
        assertNotEquals("test@gmail.com", response.getBody().getEmail());
        assertNotEquals("password", response.getBody().getPassword());
    }

    @Test
    public void testUpdateUser() {
        User user = new User(1, "test@gmail.com", "password");
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        ResponseEntity<User> response = service.update(1L, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User updated = response.getBody();
        assertNotNull(updated);
        assertEquals(1, user.getId());

        // NOTE: Email should be encrypted and password hashed!
        assertNotEquals("test@gmail.com", response.getBody().getEmail());
        assertNotEquals("password", response.getBody().getPassword());
    }

    @Test
    public void testDeleteUser() {
        User user = new User(1, "test@gmail.com", "password");
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testLoginSuccess() {
        String password = "password";
        String hashedPassword = SecurityUtil.hashPassword(password);

        User user = new User(1, "test@gmail.com", hashedPassword);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = service.login(1L, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testLoginFail() {
        String password = "password";
        String wrongPassword = "qwerty";
        String hashedPassword = SecurityUtil.hashPassword(password);

        User user = new User(1, "test@gmail.com", hashedPassword);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = service.login(1L, wrongPassword);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
}
