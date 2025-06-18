package com.example.sms.domain.model.master.payment.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 銀行口座種別の列挙型
 */
@AllArgsConstructor
@Getter
public enum BankAccountType {
    普通("1", "普通"),
    当座("2", "当座"),
    貯蓄("3", "貯蓄"),
    その他("9", "その他");

    private final String code;
    private final String name;

    /**
     * コードから銀行口座種別を取得する
     *
     * @param code 銀行口座種別コード
     * @return 銀行口座種別
     */
    public static BankAccountType fromCode(String code) {
        if (code == null) {
            return 普通;
        }

        for (BankAccountType type : BankAccountType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        
        return 普通;
    }
}