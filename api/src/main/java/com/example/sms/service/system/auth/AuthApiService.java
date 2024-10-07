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
    final JWTRepository jwtRepository;

    public AuthApiService(JWTRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    /**
     * ユーザーJWT認証
     */
    public String authenticateUser(Authentication authentication, String userId, String password) {
        return jwtRepository.generateJwtToken(authentication);
    }

}
