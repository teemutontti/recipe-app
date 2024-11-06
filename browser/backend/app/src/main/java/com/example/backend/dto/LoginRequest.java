package com.example.backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private Long id;
    private String password;
}
