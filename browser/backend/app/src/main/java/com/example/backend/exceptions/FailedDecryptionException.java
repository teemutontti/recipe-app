package com.example.backend.exceptions;

public class FailedDecryptionException extends Exception {
    public FailedDecryptionException(String str) {
        super(str);
    }
}
