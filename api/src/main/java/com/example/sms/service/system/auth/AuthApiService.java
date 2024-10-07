package com.example.sms.service.system.auth;

import com.example.sms.domain.model.system.auth.AuthUserDetails;
import com.example.sms.infrastructure.repository.system.auth.JWTRepository;
import com.example.sms.infrastructure.security.JWTAuth.payload.response.JwtResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 認証APIサービス
 */
@Service
@Transactional
public class AuthApiService {
    final AuthenticationManager authenticationManager;
    final JWTRepository jwtRepository;

    public AuthApiService(AuthenticationManager authenticationManager, JWTRepository jwtRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtRepository = jwtRepository;
    }

    /**
     * ユーザーJWT認証
     */
    public JwtResponse authenticateUser(String userId, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtRepository.generateJwtToken(authentication);

        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return new JwtResponse(jwt, userDetails.getUsername(), roles);
    }

}
