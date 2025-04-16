package com.example.sms.domain.model.sales;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.*;

/**
 * 売上番号
 */
@Value
@NoArgsConstructor(force = true)
public class SalesNumber {
    String value;

    public SalesNumber(String salesNumber) {
        notNull(salesNumber, "売上番号は必須です");
        isTrue(salesNumber.startsWith("S"), "売上番号は先頭がSで始まる必要があります");
        matchesPattern(salesNumber, "^[A-Za-z][0-9]{9}$", "売上番号は先頭が文字、続いて9桁の数字である必要があります");
        this.value = salesNumber;
    }

    public static SalesNumber of(String salesNumber) {
        return new SalesNumber(salesNumber);
    }
}
