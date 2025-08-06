package com.example.sms.domain.model.master.payment.account.incoming;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * 入金口座コードの値オブジェクト
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentAccountCode {
    String value;

    /**
     * ファクトリーメソッド
     *
     * @param value 入金口座コード
     * @return 入金口座コードの値オブジェクト
     */
    public static PaymentAccountCode of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("入金口座コードは必須です");
        }
        return new PaymentAccountCode(value);
    }
}