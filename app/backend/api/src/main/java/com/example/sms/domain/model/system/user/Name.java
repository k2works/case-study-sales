package com.example.sms.domain.model.system.user;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 名前
 */
@Value
@NoArgsConstructor(force = true)
public class Name {
    String firstName;
    String lastName;

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * 姓
     */
    public String FirstName() {
        return firstName;
    }

    /**
     * 名
     */
    public String LastName() {
        return lastName;
    }

    /**
     * フルネーム
     */
    public String FullName() {
        return firstName + " " + lastName;
    }
}
