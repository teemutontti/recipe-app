package com.example.backend.unit.service;

import com.example.backend.entities.Food;
import com.example.backend.entities.User;
import com.example.backend.exceptions.EncryptionKeyException;
import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserService;
import com.example.backend.utils.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
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

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User(1, "test@gmail.com", "password");
        repository.deleteAll();
    }

    @Test
    public void testSaveUser_ReturnsUser() throws FailedCryptionException, EncryptionKeyException {
        when(repository.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = service.create(testUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().getId() > 0);
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertEquals("REDACTED", response.getBody().getPassword());
    }

    @Test
    public void testFindById_ReturnsUser() throws FailedCryptionException, EncryptionKeyException {
        // The service expects the email to be encrypted
        testUser.setEmail(SecurityUtil.encrypt(testUser.getEmail()));
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(testUser));

        ResponseEntity<User> response = service.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getId() > 0);
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertEquals("REDACTED", response.getBody().getPassword());
    }

    @Test
    public void testFindAll_ReturnsMultipleUsers() throws FailedCryptionException, EncryptionKeyException {
        User user1 = new User(1, "maija@gmail.com", "password");
        User user2 = new User(2, "essi@gmail.com", "qwerty");

        // The service expects the emails to be encrypted
        user1.setEmail(SecurityUtil.encrypt(user1.getEmail()));
        user2.setEmail(SecurityUtil.encrypt(user2.getEmail()));

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(repository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = service.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testUpdateUser_ReturnsUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(testUser));
        when(repository.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = service.update(1L, testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() > 0);
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertEquals("REDACTED", response.getBody().getPassword());
    }

    @Test
    public void testDeleteUser_ReturnsNullBody() {
        doNothing().when(repository).deleteById(1L);

        ResponseEntity<User> response = service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testLoginSuccess() {
        // Service expects a hashed password
        testUser.setPassword(SecurityUtil.hashPassword(testUser.getPassword()));
        when(repository.findById(1L)).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = service.login(1L, "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertEquals("REDACTED", response.getBody().getPassword());
    }

    @Test
    public void testLoginFail() {
        // Service expects a hashed password
        testUser.setPassword(SecurityUtil.hashPassword(testUser.getPassword()));
        when(repository.findById(1L)).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = service.login(1L, "wrong_password");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
}
