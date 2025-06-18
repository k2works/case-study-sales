package com.example.sms.domain.model.master.payment.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入金口座区分の列挙型
 */
@AllArgsConstructor
@Getter
public enum PaymentAccountType {
    普通("1", "普通"),
    当座("2", "当座"),
    その他("9", "その他");

    private final String code;
    private final String name;

    /**
     * コードから入金口座区分を取得する
     *
     * @param code 入金口座区分コード
     * @return 入金口座区分
     */
    public static PaymentAccountType fromCode(String code) {
        if (code == null) {
            return 普通;
        }

        for (PaymentAccountType type : PaymentAccountType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        
        return 普通;
    }
}