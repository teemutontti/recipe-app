package com.example.backend.unit.repository;

import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User(1, "test@gmail.com", "password");
        repository.deleteAll();
    }

    @Test
    public void testSaveUser_ReturnsSavedUser() {
        User saved = repository.save(testUser);

        assertNotNull(saved.getId());
        assertEquals("test@gmail.com", saved.getEmail());
        assertEquals("password", saved.getPassword());
    }

    @Test
    public void testFindById_ReturnsUser() {
        User saved = repository.save(testUser);

        User found = repository.findById(Long.valueOf(saved.getId())).get();

        assertNotNull(found);
        assertTrue(found.getId() > 0);
        assertEquals("test@gmail.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }

    @Test
    public void testFindAll_ReturnsMultipleUsers() {
        User user1 = new User(null, "maija.meikalainen@gmail.com", "password");
        User user2 = new User(null, "essi.esimerkki@gmail.com", "qwerty");

        repository.save(user1);
        repository.save(user2);

        List<User> found = repository.findAll();

        assertEquals(2, found.size());
        assertEquals("maija.meikalainen@gmail.com", found.get(0).getEmail());
        assertEquals("essi.esimerkki@gmail.com", found.get(1).getEmail());
    }

    @Test
    public void testUpdateUser_ReturnsFood() {
        User saved = repository.save(testUser);

        saved.setEmail("changed@gmail.com");
        saved.setPassword("1234");
        User updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("changed@gmail.com", updated.getEmail());
        assertEquals("1234", updated.getPassword());
    }

    @Test
    public void testDeleteUser_ReturnsEmptyList() {
        User saved = repository.save(testUser);

        repository.delete(saved);
        Optional<User> found = repository.findById(Long.valueOf(saved.getId()));

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindByEmail_ReturnsUser() {
        User saved = repository.save(testUser);

        User found = repository.findByEmail(saved.getEmail()).get();

        assertNotNull(found);
        assertTrue(found.getId() > 0);
        assertEquals("test@gmail.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }
}
