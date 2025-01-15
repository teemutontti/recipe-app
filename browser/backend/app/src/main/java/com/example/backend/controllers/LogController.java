package com.example.backend.controllers;
import com.example.backend.services.LogService;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entities.Log;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController extends BaseController<Log, LogService> {
    @GetMapping("/by-date")
    public ResponseEntity<List<Log>> getLogsByDate(@RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return service.getLogsByDate(parsedDate);
    }
}