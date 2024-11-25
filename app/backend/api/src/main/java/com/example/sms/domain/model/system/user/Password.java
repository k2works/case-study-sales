package com.example.sms.domain.model.system.user;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * パスワード
 */
@Value
@NoArgsConstructor(force = true)
public class Password {
    String value;

    public Password(String value) {
        if (value == null || value.isEmpty()) {
            this.value = "";
        } else {
            checkPolicy(value);
            this.value = value;
        }
    }

    private void checkPolicy(String value) {
        if (value.length() < 8) {
            throw new PasswordException("パスワードは8文字以上である必要があります");
        }

        String regex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.find()) throw new PasswordException("パスワードは小文字、大文字、数字を含む必要があります");
    }

    /**
     * パスワード
     */
    public String Value() {
        return value;
    }
}
