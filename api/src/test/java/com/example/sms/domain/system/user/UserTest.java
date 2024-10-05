package com.example.sms.domain.system.user;

import com.example.sms.domain.model.system.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("認証・認可ドメイン")
public class UserTest {
    @Nested
    class ユーザー {
        @Test
        void ユーザーを生成できる() {
            User user = User.of("U999999", "a234567Z", "firstName", "lastName", RoleName.USER);
            assertEquals(new UserId("U999999"), user.getUserId());
            assertEquals(new Password("a234567Z"), user.getPassword());
            assertEquals("firstName", user.getFirstName());
            assertEquals("lastName", user.getLastName());
            assertEquals(RoleName.USER, user.getRoleName());
        }

        @Test
        void ユーザーIDが未入力の場合は生成できない() {
            assertThrows(UserException.class, () -> User.of(null, "password", "テスト", "太郎", RoleName.USER));
        }

        @Test
        public void ユーザーIDは先頭の一文字目がUで始まる6桁の数字である() {
            assertThrows(UserIdException.class, () -> User.of("1", "password", "テスト", "太郎", RoleName.USER));
            assertThrows(UserIdException.class, () -> User.of("X123456", "password", "テスト", "太郎", RoleName.USER));
            assertThrows(UserIdException.class, () -> User.of("U12345", "password", "テスト", "太郎", RoleName.USER));
            assertThrows(UserIdException.class, () -> User.of("Uabcdef", "password", "テスト", "太郎", RoleName.USER));
        }

        @Test
        public void パスワードが未入力の場合は空の値を設定する() {
            User user = User.of("U999999", null, "テスト", "太郎", RoleName.USER);
            assertTrue(user.getPassword().Value().isEmpty());
        }

        @Test
        public void パスワードは少なくとも8文字以上であること() {
            assertThrows(PasswordException.class, () -> User.of("U999999", "pass", "テスト", "太郎", RoleName.USER));
        }

        @Test
        public void パスワードは小文字大文字数字を含むこと() {
            assertThrows(PasswordException.class, () -> User.of("U999999", "12345678", "テスト", "太郎", RoleName.USER));
            assertThrows(PasswordException.class, () -> User.of("U999999", "a2345678", "テスト", "太郎", RoleName.USER));
            assertThrows(PasswordException.class, () -> User.of("U999999", "A2345678", "テスト", "太郎", RoleName.USER));
        }
    }
}
