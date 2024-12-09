package com.example.sms.domain.model.system.user;

/**
 * ユーザーID例外
 */
public class UserIdException extends RuntimeException {
    public UserIdException(String message) {
        super(message);
    }
}
