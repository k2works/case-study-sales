package com.example.sms.service;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}