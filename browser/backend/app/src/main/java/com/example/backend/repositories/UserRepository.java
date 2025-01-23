package com.example.backend.repositories;
import com.example.backend.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {}