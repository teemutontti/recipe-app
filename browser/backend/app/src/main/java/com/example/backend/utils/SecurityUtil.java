package com.example.backend.utils;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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

    public static String encrypt(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromEnv());

        byte[] bytes = str.getBytes(Charset.defaultCharset());
        byte[] encryptedBytes = cipher.doFinal(bytes);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromEnv());

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(str));
        return new String(decryptedBytes);
    }

    private static SecretKey getKeyFromEnv() {
        String base64Key = System.getenv("AES_KEY");
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
