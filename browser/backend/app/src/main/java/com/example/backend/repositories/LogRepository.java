package com.example.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.entities.Log;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByDateAndUserId(LocalDate date, Integer userId);
    List<Log> findByUserId(Integer id);
}