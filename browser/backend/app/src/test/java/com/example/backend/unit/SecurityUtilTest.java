package com.example.backend.unit;

import com.example.backend.entities.User;
import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.exceptions.FailedEncryptionException;
import com.example.backend.utils.SecurityUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class SecurityUtilTest {

    private final String TEST_KEY = "zGgUBphf34jh0gUacOo6Qu3knI8bXxYjKfbaDzq9Nts=";
    private final SecretKey SECRET_KEY = SecurityUtil.genKey(TEST_KEY);

    @Test
    void testHashPassword() {
        String str = "password";
        String hashed = SecurityUtil.hashPassword(str);
        assertNotEquals(str, hashed);
        assertTrue(hashed.length() > str.length());
    }

    @Test
    void testCheckPassword() {
        String hashed = SecurityUtil.hashPassword("password");
        assertTrue(SecurityUtil.checkPassword("password", hashed));
        assertFalse(SecurityUtil.checkPassword("Password", hashed));
    }

    @Test
    void testEncrypt() throws FailedCryptionException {
        String str = "secret_text";
        String encryptedStr = SecurityUtil.encrypt(str, SECRET_KEY);
        assertNotEquals(str, encryptedStr);
    }

    @Test
    void testDecrypt() throws FailedCryptionException {
        String str = "secret_text";
        String encryptedStr = SecurityUtil.encrypt(str, SECRET_KEY);
        String decryptedStr = SecurityUtil.decrypt(encryptedStr, SECRET_KEY);
        assertEquals(str, decryptedStr);
    }

    @Test
    void testEncryptUser() throws FailedCryptionException {
        User user = new User(1, "test@gmail.com", "password");
        User decryptedUser = SecurityUtil.encryptUser(user, SECRET_KEY);
        assertNotEquals("test@gmail.com", decryptedUser.getEmail());
    }

    @Test
    void testDecryptUser() throws FailedCryptionException {
        User user = new User(1, "test@gmail.com", "password");
        User encryptedUser = SecurityUtil.encryptUser(user, SECRET_KEY);
        User decryptedUser = SecurityUtil.decryptUser(encryptedUser, SECRET_KEY);
        assertEquals("test@gmail.com", decryptedUser.getEmail());
    }
}