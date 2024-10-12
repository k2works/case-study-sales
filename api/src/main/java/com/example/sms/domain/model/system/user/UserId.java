package com.example.sms.domain.model.system.user;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * ユーザーID
 */
@Value
@NoArgsConstructor(force = true)
public class UserId {
    String value;

    public UserId(String value) {
        if (value.charAt(0) != 'U') throw new UserIdException("ユーザーIDの先頭はUから始まります");
        if (value.length() != 7) throw new UserIdException("ユーザーIDの長さは7文字です");
        StringBuilder sb = new StringBuilder(value);
        String result = sb.substring(1, 7);
        if (!result.matches("\\d+")) throw new UserIdException("ユーザーIDは数字です");

        this.value = value;
    }

    /**
     * ユーザーID
     */
    public String Value() {
        return value;
    }
}
