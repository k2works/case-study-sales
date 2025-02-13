package com.example.sms.domain.model.system.user;

import lombok.NoArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * ユーザーID
 */
@Value
@NoArgsConstructor(force = true)
public class UserId {
    String value;

    public UserId(String value) {
        try {
            isTrue(value.charAt(0) == 'U', "ユーザーIDの先頭はUから始まります");
            isTrue(value.length() == 7, "ユーザーIDの長さは7文字です");
            String numericPart = value.substring(1); // index 1 以降の部分を取得
            Validate.matchesPattern(numericPart, "\\d+", "ユーザーIDの2文字目以降は数字である必要があります");

            this.value = value;
        } catch (IllegalArgumentException e) {
            throw new UserIdException(e.getMessage());
        }
    }

    public static UserId of(String userId) {
        if (userId == null) return null;
        return new UserId(userId);
    }

    /**
     * ユーザーID
     */
    public String Value() {
        return value;
    }
}