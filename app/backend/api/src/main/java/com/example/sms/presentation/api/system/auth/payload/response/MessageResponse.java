package com.example.sms.presentation.api.system.auth.payload.response;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

}
