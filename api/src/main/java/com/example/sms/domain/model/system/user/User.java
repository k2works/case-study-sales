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
    String userId;

    String password;

    String firstName;

    String lastName;

    RoleName roleName;
}
