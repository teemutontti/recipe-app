package com.example.backend.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entities.Log;

@RestController
@RequestMapping("/api/logs")
public class LogController extends BaseController<Log> {}