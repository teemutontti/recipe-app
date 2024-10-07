package com.example.backend.exceptions;

public class FailedEncryptionException extends Exception {
    public FailedEncryptionException (String str) {
        super(str);
    }
}
