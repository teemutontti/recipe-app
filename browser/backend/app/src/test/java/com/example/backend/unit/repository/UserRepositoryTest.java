package com.example.backend.unit.repository;

import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
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
    public void testFindById() {
        User saved = repository.save(testUser);

        Long savedId = Long.valueOf(saved.getId());
        Optional<User> found = repository.findById(savedId);

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    public void testSaveUser() {
        User saved = repository.save(testUser);

        assertNotNull(saved.getId());
        assertEquals("test@gmail.com", saved.getEmail());
        assertEquals("password", saved.getPassword());
    }

    @Test
    public void testUpdateUser() {
        User saved = repository.save(testUser);

        saved.setEmail("changed@gmail.com");
        saved.setPassword("qwerty");
        User updated = repository.save(saved);

        assertEquals("changed@gmail.com", updated.getEmail());
        assertEquals("qwerty", updated.getPassword());
    }

    @Test
    public void testDelete() {
        User saved = repository.save(testUser);

        repository.delete(saved);
        Long savedId = Long.valueOf(saved.getId());
        Optional<User> found = repository.findById(savedId);

        assertFalse(found.isPresent());
    }
}
