package com.example.sms.service.system.user;

import com.example.sms.domain.model.system.user.Name;
import com.example.sms.domain.model.system.user.Password;
import com.example.sms.domain.model.system.user.User;
import com.example.sms.domain.model.system.user.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("ユーザーレポジトリ")
class UserRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("root")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired
    private UserRepository repository;

    private static User getUser() {
        return User.of("U999999", "a234567Z", "firstName", "lastName", RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("ユーザー一覧を取得できる")
    void shouldRetrieveAllUsers() {
        User user = getUser();
        repository.save(user);
        assertEquals(1, repository.selectAll().size());
    }

    @Test
    @DisplayName("ユーザーを登録できる")
    void shouldRegisterAUser() {
        User user = getUser();
        repository.save(user);
        Optional<User> actual = repository.findById(user.getUserId().Value());
        assertTrue(actual.isPresent());
        assertEquals(user.getUserId(), actual.get().getUserId());
        assertEquals(user.getName(), actual.get().getName());
        assertEquals(user.getPassword(), actual.get().getPassword());
        assertEquals(user.getRoleName(), actual.get().getRoleName());
    }

    @Test
    @DisplayName("ユーザーを更新できる")
    void shouldUpdateAUser() {
        User user = getUser();
        repository.save(user);
        User updateUser = new User(user.getUserId(), new Password("b234567Z"), new Name("firstName2", "lastName2"), RoleName.ADMIN);
        repository.save(updateUser);
        Optional<User> actual = repository.findById(user.getUserId().Value());
        assertTrue(actual.isPresent());
        assertEquals(updateUser.getUserId(), actual.get().getUserId());
        assertEquals(updateUser.getName(), actual.get().getName());
        assertEquals(updateUser.getPassword(), actual.get().getPassword());
        assertEquals(updateUser.getRoleName(), actual.get().getRoleName());
    }

    @Test
    @DisplayName("ユーザーを削除できる")
    void shouldDeleteAUser() {
        User user = getUser();
        repository.save(user);
        repository.deleteById("userId");
        Optional<User> actual = repository.findById("userId");
        assertTrue(actual.isEmpty());
    }
}
