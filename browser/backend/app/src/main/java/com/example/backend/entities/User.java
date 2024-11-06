package com.example.backend.entities;

import com.example.backend.utils.SecurityUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements BaseEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;

    @JsonIgnore // Ignoring password field when serializing the object so password isn't shared
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