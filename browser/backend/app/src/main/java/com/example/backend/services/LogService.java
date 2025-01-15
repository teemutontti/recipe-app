package com.example.backend.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.backend.entities.Log;
import com.example.backend.repositories.LogRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class LogService extends BaseService<Log> {

    @Autowired
    private LogRepository logRepository;

    @Override
    protected LogRepository getRepository() {
        return logRepository;
    }

    public ResponseEntity<List<Log>> getLogsByDate(LocalDate date) {
        try {
            List<Log> logs = logRepository.findByDate(date);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}