package com.example.backend.unit.service;

import com.example.backend.dto.UserDto;
import com.example.backend.entities.User;
import com.example.backend.exceptions.EncryptionKeyException;
import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserService;
import com.example.backend.utils.SecurityUtil;
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

    private UserDto testUser;

    @BeforeEach
    public void setup() {
        testUser = new UserDto(null, "test@gmail.com", "password");
        repository.deleteAll();
    }

    @Test
    public void testSaveUser_ReturnsUser() {
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<User> response = service.create(testUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertNull(response.getBody().getPassword());
    }

    @Test
    public void testFindById_ReturnsUser() throws FailedCryptionException, EncryptionKeyException {
        // The service expects the email to be encrypted
        testUser.setEmail(SecurityUtil.encrypt(testUser.getEmail()));
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(testUser.toUser()));

        ResponseEntity<User> response = service.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertNull(response.getBody().getPassword());
    }

    @Test
    public void testFindAll_ReturnsMultipleUsers() throws FailedCryptionException, EncryptionKeyException {
        // The service expects the emails to be encrypted
        String email = SecurityUtil.encrypt("maija@gmail.com");

        User user1 = new User(1, email, "password");
        User user2 = new User(2, email, "password");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(repository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = service.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertNull(response.getBody().get(0).getPassword());
        assertNull(response.getBody().get(1).getPassword());
    }

    @Test
    public void testUpdateUser_ReturnsUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(testUser.toUser()));
        when(repository.save(any(User.class))).thenReturn(testUser.toUser());

        ResponseEntity<User> response = service.update(1L, testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertNull(response.getBody().getPassword());
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
        when(repository.findById(1L)).thenReturn(Optional.of(testUser.toUser()));

        ResponseEntity<User> response = service.login(1L, "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertNull(response.getBody().getPassword());
    }

    @Test
    public void testLoginFail() {
        // Service expects a hashed password
        testUser.setPassword(SecurityUtil.hashPassword(testUser.getPassword()));
        when(repository.findById(1L)).thenReturn(Optional.of(testUser.toUser()));

        ResponseEntity<User> response = service.login(1L, "wrong_password");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }
}
