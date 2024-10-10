package com.example.sms.domain.model.system.user;

/**
 * ユーザー例外
 */
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
