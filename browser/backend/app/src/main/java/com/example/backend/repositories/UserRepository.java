package com.example.backend.repositories;
import com.example.backend.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}