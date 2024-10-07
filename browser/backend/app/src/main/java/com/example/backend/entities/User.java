package com.example.backend.entities;

import com.example.backend.utils.SecurityUtil;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User implements BaseEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String password;

    @Override
    public boolean update(User user) {
        try {
            if (user.getEmail() != null) {
                String encryptedEmail = SecurityUtil.encrypt(user.getEmail());
                setEmail(encryptedEmail);
            }
            if (user.getPassword() != null) {
                String hashedPassword = SecurityUtil.hashPassword(user.getPassword());
                setPassword(hashedPassword);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}