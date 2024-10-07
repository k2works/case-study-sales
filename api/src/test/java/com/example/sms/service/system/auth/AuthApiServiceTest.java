package com.example.sms.service.system.auth;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.user.User;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    TestDataFactory testDataFactory;

    @MockBean
    AuthRepository authRepository;

    @Nested
    @DisplayName("ユーザーの認証")
    class UserAuthentication {

        @BeforeEach
        void setUp() {
            testDataFactory.setUpForAuthApiService();
        }

        @Test
        @DisplayName("ユーザーを認証する")
        void shouldAuthenticateUser() {
            User user = testDataFactory.User();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // モックの設定
            Mockito.when(authRepository.generateJwtToken(Mockito.nullable(Authentication.class)))
                    .thenReturn("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVMDAwMDA3IiwiaWF0IjoxNjU2NzMxODc3LCJleHAiOjE2NTY4MTgyNzd9.2JGYfw4c2P4EzCFFuCN7kf5fMihSXEVfLZSRnC5OOOn4vpPy9QewaVXTheUzsv16X8Lk1bpvcAyQYSUuKj0vJA");

            // 認証メソッドの呼び出し
            String jwtToken = authApiService.authenticateUser(authentication, user.getUserId().Value(), user.getPassword().Value());

            // 検証
            assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVMDAwMDA3IiwiaWF0IjoxNjU2NzMxODc3LCJleHAiOjE2NTY4MTgyNzd9.2JGYfw4c2P4EzCFFuCN7kf5fMihSXEVfLZSRnC5OOOn4vpPy9QewaVXTheUzsv16X8Lk1bpvcAyQYSUuKj0vJA", jwtToken);
        }
    }
}
