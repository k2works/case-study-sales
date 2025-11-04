package com.example.sms.domain.model.system.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.notNull;

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

    public static User of(String userId, String password, String firstName, String lastName, RoleName roleName) {
        try {
            notNull(userId, "ユーザーIDが未入力です");
            notNull(firstName, "名前（姓）が未入力です");
            notNull(lastName, "名前（名）が未入力です");
            notNull(roleName, "ロール名が未入力です");

            return new User(new UserId(userId), new Password(password), new Name(firstName, lastName), roleName);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UserException(e.getMessage());
        }
    }
}
