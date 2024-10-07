package com.example.sms.service.system.auth;

import org.springframework.security.core.Authentication;

/**
 * 認証リポジトリ
 */
public interface AuthRepository {
    String generateJwtToken(Authentication authentication);

    String getUserNameFromJwtToke(String authToken);

    boolean validateJwtToke(String authToken);
}
