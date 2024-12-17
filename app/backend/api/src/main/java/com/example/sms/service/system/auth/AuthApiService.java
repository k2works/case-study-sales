package com.example.sms.service.system.auth;

import com.example.sms.domain.model.system.auth.AuthUserDetails;
import com.example.sms.domain.model.system.user.UserId;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    /**
     * 現在のユーザーIDの取得
     */
    public static UserId getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("User not found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthUserDetails) {
            String userId = ((AuthUserDetails) principal).getUsername();
            return UserId.of(userId);
        } else {
            throw new UsernameNotFoundException("User details not found");
        }
    }

    /**
     * 権限チェック
     */
    public static void checkPermission(String requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasPermission = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(requiredRole));

        if (!hasPermission) {
            throw new AccessDeniedException("権限がありません");
        }
    }
}
