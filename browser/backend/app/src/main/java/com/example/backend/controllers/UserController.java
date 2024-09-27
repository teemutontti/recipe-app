package com.example.backend.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entities.User;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User> {}