package com.example.sms.domain.model.system.user;

/**
 * パスワード例外
 */
public class PasswordException extends RuntimeException {
    public PasswordException(String message) {
        super(message);
    }
}
