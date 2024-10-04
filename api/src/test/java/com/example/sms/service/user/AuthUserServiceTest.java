package com.example.sms.service.user;

import com.example.sms.domain.model.RoleName;
import com.example.sms.domain.model.User;
import com.example.sms.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthUserServiceTest {
    @Autowired
    AuthService service;

    @MockBean
    UserRepository repository;

    @Test
    @DisplayName("ユーザが存在する場合、ユーザ情報を返す")
    void loadUserByUsername_WhenUserExists_ReturnsUserDetails() {
        User user = new User();
        user.setUserId("test");
        user.setPassword("pass");
        user.setFirstName("first");
        user.setLastName("last");
        user.setRoleName(RoleName.USER);
        when(repository.findById(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername("test");

        assertEquals("test", userDetails.getUsername());
    }

    @Test
    @DisplayName("ユーザが存在しない場合、UsernameNotFoundExceptionをスローする")
    void loadUserByUsername_WhenUserDoesNotExist_ThrowsUsernameNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("test"));
    }
}
