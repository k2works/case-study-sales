package com.example.sms.service.system.auth;

import com.example.sms.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisplayName("認証APIサービス")
public class AuthApiServiceTest {

    @Autowired
    AuthApiService authApiService;

    @MockBean
    AuthRepository authRepository;

    @Nested
    @DisplayName("ユーザーの認証")
    class UserAuthentication {

        @Test
        @DisplayName("ユーザーを認証する")
        void shouldAuthenticateUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // モックの設定
            Mockito.when(authRepository.generateJwtToken(Mockito.nullable(Authentication.class)))
                    .thenReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVMDAwMDA3IiwiaWF0IjoxNjU2NzMxODc3LCJleHAiOjE2NTY4MTgyNzd9.2JGYfw4c2P4EzCFFuCN7kf5fMihSXEVfLZSRnC5OOOn4vpPy9QewaVXTheUzsv16X8Lk1bpvcAyQYSUuKj0vJA");

            // 認証メソッドの呼び出し
            String jwtToken = authApiService.authenticateUser(authentication, "U999999", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK");

            // 検証
            assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVMDAwMDA3IiwiaWF0IjoxNjU2NzMxODc3LCJleHAiOjE2NTY4MTgyNzd9.2JGYfw4c2P4EzCFFuCN7kf5fMihSXEVfLZSRnC5OOOn4vpPy9QewaVXTheUzsv16X8Lk1bpvcAyQYSUuKj0vJA", jwtToken);
        }
    }
}
