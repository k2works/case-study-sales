package com.example.sms.service.system.auth;

import com.example.sms.domain.model.system.user.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

/**
 * 認証リポジトリ
 */
public interface AuthRepository {
    Optional<User> findById(String userId);

    String generateJwtToken(Authentication authentication);

    String getUserNameFromJwtToke(String authToken);

    boolean validateJwtToke(String authToken);
}
