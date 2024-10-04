package com.example.sms.service.user;

import com.example.sms.domain.model.User;
import com.example.sms.infrastructure.datasource.UserDataSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 認証サービス
 */
@Service
public class AuthService implements UserDetailsService {
    final UserDataSource userRepository;

    public AuthService(UserDataSource userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username + " is not found."));
        return new AuthUserDetails(user);
    }
}
