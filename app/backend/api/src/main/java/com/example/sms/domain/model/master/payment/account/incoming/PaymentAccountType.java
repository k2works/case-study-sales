package com.example.sms.domain.model.master.payment.account.incoming;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入金口座区分の列挙型
 */
@AllArgsConstructor
@Getter
public enum PaymentAccountType {
    銀行("1", "銀行"),
    郵便局("2", "郵便局"),
    農協("3", "農協"),
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
            return 銀行;
        }

        for (PaymentAccountType type : PaymentAccountType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        
        return 銀行;
    }
}