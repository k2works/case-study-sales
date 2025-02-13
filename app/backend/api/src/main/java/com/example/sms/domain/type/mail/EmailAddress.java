package com.example.sms.domain.type.mail;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.*;

/**
 * メール
 */
@Value
@NoArgsConstructor(force = true)
public class EmailAddress {
    String value;

    public EmailAddress(String value) {
        notNull(value, "メールアドレスは必須です");

        inclusiveBetween(15, 77, value.length(),
                "メールアドレスの長さは15文字以上77文字以下である必要があります");

        isTrue(value.indexOf("@") < 65,
                "ローカル部は64文字以下である必要があります");

        matchesPattern(value.toLowerCase(),
                "^[a-z0-9]+\\.?[a-z0-9]+@\\bexample.com$",
                "不正なメールアドレスです");

        this.value = value;
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }
}
