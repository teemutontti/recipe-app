package com.example.backend.utils;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import com.example.backend.entities.User;
import com.example.backend.exceptions.EncryptionKeyException;
import com.example.backend.exceptions.FailedDecryptionException;
import com.example.backend.exceptions.FailedEncryptionException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import java.nio.charset.Charset;
//import javax.crypto.KeyGenerator;

public class SecurityUtil {

    private final static Argon2PasswordEncoder e = new Argon2PasswordEncoder(
        16, 32, 1, 60000, 10
    );

    public static String hashPassword(String password) {
        return e.encode(password);
    }

    public static boolean checkPassword(String plain, String hashed) {
        return e.matches(plain, hashed);
    }

    public static String encrypt(String str) throws FailedEncryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());

            byte[] bytes = str.getBytes(Charset.defaultCharset());
            byte[] encryptedBytes = cipher.doFinal(bytes);

            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new FailedEncryptionException(e.getMessage());
        }
    }

    public static String decrypt(String str) throws FailedDecryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKey());

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(str));
            return new String(decryptedBytes);

        } catch (Exception e) {
            throw new FailedDecryptionException(e.getMessage());
        }
    }

    public static User encryptUser(User user) throws FailedEncryptionException {
        String encryptedEmail = encrypt(user.getEmail());
        user.setEmail(encryptedEmail);
        return user;
    }

    public static User decryptUser(User user) throws FailedDecryptionException {
        String decryptedEmail = decrypt(user.getEmail());
        user.setEmail(decryptedEmail);
        return user;
    }

    private static SecretKey getKey() throws EncryptionKeyException {
        String base64Key = System.getenv("AES_KEY");

        // Throw error if key is not found in environmental variables
        if (base64Key == null) {
            throw new EncryptionKeyException();
        }

        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
