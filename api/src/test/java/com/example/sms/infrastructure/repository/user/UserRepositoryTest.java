package com.example.sms.infrastructure.repository.user;

import com.example.sms.domain.model.RoleName;
import com.example.sms.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("ユーザーレポジトリ")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private static User getUser() {
        return new User("userId", "password", "firstName", "lastName", RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("ユーザー一覧を取得できる")
    void shouldRetrieveAllUsers() {
        User user = getUser();
        repository.insert(user);
        assertEquals(1, repository.selectAll().size());
    }

    @Test
    @DisplayName("ユーザーを登録できる")
    void shouldRegisterAUser() {
        User user = getUser();
        repository.insert(user);
        Optional<User> actual = repository.findById("userId");
        assertTrue(actual.isPresent());
        assertEquals(user.getUserId(), actual.get().getUserId());
        assertEquals(user.getFirstName(), actual.get().getFirstName());
        assertEquals(user.getLastName(), actual.get().getLastName());
        assertEquals(user.getPassword(), actual.get().getPassword());
        assertEquals(user.getRoleName(), actual.get().getRoleName());
    }

    @Test
    @DisplayName("ユーザーを更新できる")
    void shouldUpdateAUser() {
        User user = getUser();
        repository.insert(user);
        User updateUser = new User(user.getUserId(), "password2", "firstName2", "lastName2", RoleName.ADMIN);
        repository.update(updateUser);
        Optional<User> actual = repository.findById("userId");
        assertTrue(actual.isPresent());
        assertEquals(updateUser.getUserId(), actual.get().getUserId());
        assertEquals(updateUser.getFirstName(), actual.get().getFirstName());
        assertEquals(updateUser.getLastName(), actual.get().getLastName());
        assertEquals(updateUser.getPassword(), actual.get().getPassword());
        assertEquals(updateUser.getRoleName(), actual.get().getRoleName());
    }

    @Test
    @DisplayName("ユーザーを削除できる")
    void shouldDeleteAUser() {
        User user = getUser();
        repository.insert(user);
        repository.deleteById("userId");
        Optional<User> actual = repository.findById("userId");
        assertTrue(actual.isEmpty());
    }
}
