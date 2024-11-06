package com.example.backend.unit;

import com.example.backend.controllers.UserController;
import com.example.backend.entities.User;
import com.example.backend.services.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(
            new User(1, "1@gmail.com", "password1"),
            new User(2, "2@gmail.com", "password2"),
            new User(3, "3@gmail.com", "password3")
        );
        when(userService.getAll()).thenReturn(new ResponseEntity<>(users, HttpStatus.OK));

        ResponseEntity<List<User>> response = userController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().toArray().length);
        assertEquals("2@gmail.com", response.getBody().get(1).getEmail());
        assertEquals("password2", response.getBody().get(1).getPassword());
    }

    @Test
    void testGetUserById() {
        User user = new User(1, "test@gmail.com", "password");
        when(userService.getById(1L)).thenReturn(new ResponseEntity<>(user, HttpStatus.OK));

        ResponseEntity<User> response = userController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertEquals("password", response.getBody().getPassword());
    }

    @Test
    void testGetUserByIdBadRequest() {
        when(userService.getById(900L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<User> response = userController.getById(900L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateUser() {
        User user = new User(1, "test@gmail.com", "password");
        when(userService.create(user)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        ResponseEntity<User> response = userController.create(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateUserBadRequest() {
        User invalidUser = new User(-2, "", "");
        when(userService.create(invalidUser)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        ResponseEntity<User> response = userController.create(invalidUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateUser() {
        // Original user: User(1, "test@gmail.com", "password")
        User updatedUser = new User(1, "new@gmail.com", "password");

        // Creating user with null values and setting only email
        User updatableUser = new User();
        updatableUser.setEmail("new@gmail.com");

        when(userService.update(1L, updatableUser)).thenReturn(new ResponseEntity<>(updatedUser, HttpStatus.OK));

        ResponseEntity<User> response = userController.update(1L, updatableUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("new@gmail.com", response.getBody().getEmail());
        assertEquals("password", response.getBody().getPassword());
    }

    @Test
    void testUpdateUserBadRequest() {
        // Creating user with null values and setting NO new values
        User updatableUser = new User();

        when(userService.update(1L, updatableUser)).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        ResponseEntity<User> response = userController.update(1L, updatableUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteUser() {
        when(userService.delete(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<User> response = userController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}