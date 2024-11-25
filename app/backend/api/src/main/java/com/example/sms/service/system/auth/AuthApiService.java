package com.example.sms.service.system.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 認証APIサービス
 */
@Service
@Transactional
public class AuthApiService {
    final AuthRepository authRepository;

    public AuthApiService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * ユーザーJWT認証
     */
    public String authenticateUser(Authentication authentication, String userId, String password) {
        return authRepository.generateJwtToken(authentication);
    }

    /**
     * JWTトークンの検証
     */
    public boolean validateJwtToken(String authToken) {
        return authRepository.validateJwtToke(authToken);
    }

    /**
     * JWTトークンの取得
     */
    public boolean validateJwtToke(String jwt) {
        return authRepository.validateJwtToke(jwt);
    }

    /**
     * JWTトークンからユーザー名の取得
     */
    public String getUserNameFromJwtToke(String jwt) {
        return authRepository.getUserNameFromJwtToke(jwt);
    }
}
