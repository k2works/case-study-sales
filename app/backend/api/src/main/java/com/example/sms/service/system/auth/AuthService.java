package com.example.sms.service.system.auth;

import com.example.sms.domain.model.system.auth.AuthUserDetails;
import com.example.sms.domain.model.system.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 認証サービス
 */
@Service
public class AuthService implements UserDetailsService {
    final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * ユーザー認証
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username + " is not found."));
        return new AuthUserDetails(user);
    }
}
