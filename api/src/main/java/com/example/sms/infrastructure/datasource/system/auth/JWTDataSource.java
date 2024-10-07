package com.example.sms.infrastructure.datasource.system.auth;

import com.example.sms.infrastructure.security.JWTAuth.JwtUtils;
import com.example.sms.service.system.auth.AuthRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

@Repository
public class JWTDataSource implements AuthRepository {
    final JwtUtils jwtUtils;

    public JWTDataSource(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String generateJwtToken(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }

    @Override
    public String getUserNameFromJwtToke(String authToken) {
        return jwtUtils.getUserNameFromJwtToke(authToken);
    }

    @Override
    public boolean validateJwtToke(String authToken) {
        return jwtUtils.validateJwtToke(authToken);
    }
}
