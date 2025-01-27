package com.example.sms.service;

public class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }
}