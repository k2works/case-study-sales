package com.example.sms.service.system.user;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.system.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisplayName("ユーザー管理サービス")
public class UserManagementServiceTest {
    @Autowired
    UserManagementService userManagementService;

    @Autowired
    TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForUserManagementService();
    }

    @Nested
    @DisplayName("ユーザー管理")
    class UserManagementTests {
        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        @DisplayName("ユーザー一覧を取得できる")
        void getAllUsers() {
            UserList result = userManagementService.selectAll();
            assertEquals(2, result.asList().size());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        @DisplayName("ユーザーを新規登録できる")
        void registerNewUser() {
            User newUser = User.of("U999990", "$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK", "first", "last", RoleName.USER);
            userManagementService.register(newUser);
            UserList result = userManagementService.selectAll();
            assertEquals(3, result.asList().size());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        @DisplayName("ユーザーの登録情報を編集できる")
        void editUserDetails() {
            User user = TestDataFactoryImpl.getUser();
            User updateUser = new User(user.getUserId(), user.getPassword(), new Name("editedFirstName", "editedLastName"), user.getRoleName());
            userManagementService.save(updateUser);
            User result = userManagementService.find(user.getUserId());
            assertEquals("editedFirstName", result.getName().FirstName());
            assertEquals("editedLastName", result.getName().LastName());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        @DisplayName("ユーザーを削除できる")
        void deleteUser() {
            User user = TestDataFactoryImpl.getUser();
            userManagementService.delete(user.getUserId());
            UserList result = userManagementService.selectAll();
            assertEquals(1, result.asList().size());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        @DisplayName("ユーザーを検索できる")
        void searchUser() {
            User user = TestDataFactoryImpl.getUser();
            User result = userManagementService.find(user.getUserId());
            assertEquals(new UserId("U999999"), result.getUserId());
        }
    }
}
