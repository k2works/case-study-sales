package com.example.sms.infrastructure.repository.system.auth;

import org.springframework.security.core.Authentication;

public interface JWTRepository {
    String generateJwtToken(Authentication authentication);

    String getUserNameFromJwtToke(String authToken);

    boolean validateJwtToke(String authToken);
}
