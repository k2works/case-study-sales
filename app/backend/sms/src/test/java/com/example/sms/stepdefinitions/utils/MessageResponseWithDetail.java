package com.example.sms.stepdefinitions.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class MessageResponseWithDetail {
    private String message;
    private List<Map<String, String>> details;
}
