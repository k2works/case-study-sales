package com.example.sms.service.system.auth;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.service.system.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@IntegrationTest
@DisplayName("認証サービス")
public class AuthServiceTest {
    @Autowired
    AuthService service;

    @MockBean
    UserRepository repository;

    @Autowired
    TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForAuthApiService();
    }

    @Test
    @DisplayName("ユーザが存在する場合、ユーザ情報を返す")
    void loadUserByUsername_WhenUserExists_ReturnsUserDetails() {
        User user = testDataFactory.User();

        when(repository.findById(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername(user.getUserId().Value());

        assertEquals("U999999", userDetails.getUsername());
    }

    @Test
    @DisplayName("ユーザが存在しない場合、UsernameNotFoundExceptionをスローする")
    void loadUserByUsername_WhenUserDoesNotExist_ThrowsUsernameNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("U999990"));
    }
}
