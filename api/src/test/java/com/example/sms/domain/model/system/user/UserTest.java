package com.example.sms.domain.model.system.user;

import com.example.sms.domain.type.user.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ユーザー")
public class UserTest {

    @Test
    @DisplayName("ユーザーを生成できる")
    void canCreateUser() {
        User user = User.of("U999999", "a234567Z", "firstName", "lastName", RoleName.USER);
        assertEquals(new UserId("U999999"), user.getUserId());
        assertEquals(new Password("a234567Z"), user.getPassword());
        assertEquals("firstName", user.getName().FirstName());
        assertEquals("lastName", user.getName().LastName());
        assertEquals("firstName lastName", user.getName().FullName());
        assertEquals(RoleName.USER, user.getRoleName());
    }

    @Test
    @DisplayName("ユーザーIDが未入力の場合は生成できない")
    void cannotCreateUserWhenUserIdIsMissing() {
        assertThrows(UserException.class, () -> User.of(null, "password", "テスト", "太郎", RoleName.USER));
    }

    @Test
    @DisplayName("ユーザーIDは先頭の一文字目がUで始まる6桁の数字である")
    public void userIdMustStartWithUAndBeSixDigitNumber() {
        assertThrows(UserIdException.class, () -> User.of("1", "password", "テスト", "太郎", RoleName.USER));
        assertThrows(UserIdException.class, () -> User.of("X123456", "password", "テスト", "太郎", RoleName.USER));
        assertThrows(UserIdException.class, () -> User.of("U12345", "password", "テスト", "太郎", RoleName.USER));
        assertThrows(UserIdException.class, () -> User.of("Uabcdef", "password", "テスト", "太郎", RoleName.USER));
    }

    @Test
    @DisplayName("パスワードが未入力の場合は空の値を設定する")
    public void passwordIsEmptyWhenMissing() {
        User user = User.of("U999999", null, "テスト", "太郎", RoleName.USER);
        assertTrue(user.getPassword().Value().isEmpty());
    }

    @Test
    @DisplayName("パスワードは少なくとも8文字以上であること")
    public void passwordMustBeAtLeastEightCharacters() {
        assertThrows(PasswordException.class, () -> User.of("U999999", "pass", "テスト", "太郎", RoleName.USER));
    }

    @Test
    @DisplayName("パスワードは小文字大文字数字を含むこと")
    public void passwordMustContainUppercaseLowercaseAndDigits() {
        assertThrows(PasswordException.class, () -> User.of("U999999", "12345678", "テスト", "太郎", RoleName.USER));
        assertThrows(PasswordException.class, () -> User.of("U999999", "a2345678", "テスト", "太郎", RoleName.USER));
        assertThrows(PasswordException.class, () -> User.of("U999999", "A2345678", "テスト", "太郎", RoleName.USER));
    }

    @Test
    @DisplayName("名前が未入力の場合は生成できない")
    public void cannotCreateUserWhenNameIsMissing() {
        assertThrows(UserException.class, () -> User.of("U999999", "password", null, "太郎", RoleName.USER));
        assertThrows(UserException.class, () -> User.of("U999999", "password", "テスト", null, RoleName.USER));
    }

    @Test
    @DisplayName("役割が未入力の場合は生成できない")
    public void cannotCreateUserWhenRoleIsMissing() {
        assertThrows(UserException.class, () -> User.of("U999999", "password", "テスト", "太郎", null));
    }
}
