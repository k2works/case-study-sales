package com.example.sms.domain.model.system.user;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

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
        try {
            notBlank(value, "パスワードは必須です");
            isTrue(value.length() >= 8, "パスワードは8文字以上である必要があります");

            boolean hasDigit = value.chars().anyMatch(Character::isDigit);
            boolean hasLower = value.chars().anyMatch(Character::isLowerCase);
            boolean hasUpper = value.chars().anyMatch(Character::isUpperCase);

            isTrue(
                    hasDigit && hasLower && hasUpper,
                    "パスワードは小文字、大文字、数字を含む必要があります"
            );
        } catch (RuntimeException e) {
            throw new PasswordException(e.getMessage());
        }
    }

    /**
     * パスワード
     */
    public String Value() {
        return value;
    }
}
