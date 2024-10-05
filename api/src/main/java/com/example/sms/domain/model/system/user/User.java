package com.example.sms.domain.model.system.user;

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

    String password;

    String firstName;

    String lastName;

    RoleName roleName;

    static public User of(String userId, String password, String firstName, String lastName, RoleName roleName) {
        if (userId == null) {
            throw new UserException("ユーザーIDが未入力です");
        }
        return new User(new UserId(userId), password, firstName, lastName, roleName);
    }
}
