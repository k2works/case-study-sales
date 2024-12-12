package com.example.sms.presentation.api.system.auth.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 認証リクエスト
 */
@Schema(description = "認証リクエスト")
public class LoginRequest {
    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

