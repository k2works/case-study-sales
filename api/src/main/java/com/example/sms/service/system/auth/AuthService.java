package com.example.sms.service.system.auth;

import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.repository.user.UserRepository;
import com.example.sms.domain.model.system.auth.AuthUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 認証サービス
 */
@Service
public class AuthService implements UserDetailsService {
    final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username + " is not found."));
        return new AuthUserDetails(user);
    }
}
