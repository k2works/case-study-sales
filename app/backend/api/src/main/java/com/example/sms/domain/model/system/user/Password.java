package com.example.sms.domain.model.system.user;

import lombok.NoArgsConstructor;
import lombok.Value;

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
        if (value == null || value.length() < 8) {
            throw new PasswordException("パスワードは8文字以上である必要があります");
        }

        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;

        for (char c : value.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;

            // すべての条件を満たしたら早期終了する
            if (hasDigit && hasLower && hasUpper) break;
        }

        if (!hasDigit || !hasLower || !hasUpper) {
            throw new PasswordException("パスワードは小文字、大文字、数字を含む必要があります");
        }
    }

    /**
     * パスワード
     */
    public String Value() {
        return value;
    }
}
