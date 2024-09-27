package com.example.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entities.Log;

public interface LogRepository extends JpaRepository<Log, Long> {}