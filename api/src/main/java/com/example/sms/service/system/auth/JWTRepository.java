package com.example.sms.service.system.auth;

import org.springframework.security.core.Authentication;

public interface JWTRepository {
    String generateJwtToken(Authentication authentication);

    String getUserNameFromJwtToke(String authToken);

    boolean validateJwtToke(String authToken);
}
