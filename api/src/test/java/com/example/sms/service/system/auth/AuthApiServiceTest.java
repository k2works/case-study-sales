package com.example.sms.service.system.auth;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.infrastructure.security.JWTAuth.payload.response.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisplayName("認証APIサービス")
public class AuthApiServiceTest {
    @Autowired
    AuthApiService authApiService;

    @Autowired
    TestDataFactory testDataFactory;

    @Nested
    @DisplayName("ユーザーの認証")
    class UserAuthentication {
        @MockBean
        AuthApiService authApiServiceMock;

        @BeforeEach
        void setUp() {
            testDataFactory.setUpForAuthApiService();
        }

        @Test
        @DisplayName("ユーザーを認証する")
        void shouldAuthenticateUser() {
            User user = testDataFactory.User();
            Mockito.when(authApiServiceMock.authenticateUser(Mockito.any(), Mockito.any()))
                    .thenReturn(new JwtResponse("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVMDAwMDA3IiwiaWF0IjoxNjU2NzMxODc3LCJleHAiOjE2NTY4MTgyNzd9.2JGYfw4c2P4EzCFFuCN7kf5fMihSXEVfLZSRnC5OOOn4vpPy9QewaVXTheUzsv16X8Lk1bpvcAyQYSUuKj0vJA", "user", List.of("USER")));
            JwtResponse result = authApiServiceMock.authenticateUser(user.getUserId(), user.getPassword());

            assertEquals("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJVMDAwMDA3IiwiaWF0IjoxNjU2NzMxODc3LCJleHAiOjE2NTY4MTgyNzd9.2JGYfw4c2P4EzCFFuCN7kf5fMihSXEVfLZSRnC5OOOn4vpPy9QewaVXTheUzsv16X8Lk1bpvcAyQYSUuKj0vJA", result.getAccessToken());
            assertEquals(user.getUserId(), result.getUserId());
            assertEquals(List.of("USER"), result.getRoles());
        }
    }
}
