package com.example.backend.entities;

import com.example.backend.utils.SecurityUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User implements BaseEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    /**
     * Encrypts the user's email address
     * @return boolean - true if encryption is successful, false otherwise
     */
    public boolean encrypt() {
        try {
            String encryptedEmail = SecurityUtil.encrypt(this.email);
            setEmail(encryptedEmail);
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    /**
     * Decrypts the user's email address
     * @return boolean - true if decryption is successful, false otherwise
     */
    public boolean decrypt() {
        try {
            String decryptedEmail = SecurityUtil.decrypt(getEmail());
            setEmail(decryptedEmail);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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