package com.example.sms.presentation.api.system.auth.payload.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class MessageResponseWithDetail {
    private String message;
    private List<Map<String, String>> details;

    public MessageResponseWithDetail(String message, List<Map<String, String>> details) {
        this.message = message;
        this.details = details;
    }

}
