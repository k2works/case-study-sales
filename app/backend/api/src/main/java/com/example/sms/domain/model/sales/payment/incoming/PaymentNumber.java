package com.example.sms.domain.model.sales.payment.incoming;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * 入金番号
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentNumber {
    String value;

    /**
     * ファクトリーメソッド
     *
     * @param value 入金番号
     * @return 入金番号の値オブジェクト
     */
    public static PaymentNumber of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("入金番号は必須です");
        }
        return new PaymentNumber(value);
    }
}