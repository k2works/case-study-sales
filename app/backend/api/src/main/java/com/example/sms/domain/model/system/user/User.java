package com.example.sms.domain.model.system.user;

import com.example.sms.domain.type.user.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * ユーザー
 */
@Value
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {
    UserId userId;

    Password password;

    Name name;

    RoleName roleName;

    static public User of(String userId, String password, String firstName, String lastName, RoleName roleName) {
        if (userId == null) {
            throw new UserException("ユーザーIDが未入力です");
        }
        if (firstName == null || lastName == null) {
            throw new UserException("名前が未入力です");
        }
        if (roleName == null) {
            throw new UserException("ロール名が未入力です");
        }
        return new User(new UserId(userId), new Password(password), new Name(firstName, lastName), roleName);
    }
}
