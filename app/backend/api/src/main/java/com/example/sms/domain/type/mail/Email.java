package com.example.sms.domain.type.mail;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.regex.Pattern;

/**
 * メール
 */
@Value
@NoArgsConstructor(force = true)
public class Email {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    String value;

    public Email(String value) {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("メールアドレスの形式が正しくありません: " + value);
        }
        this.value = value;
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
