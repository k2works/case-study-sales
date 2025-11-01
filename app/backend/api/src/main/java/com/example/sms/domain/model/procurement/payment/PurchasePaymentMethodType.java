package com.example.sms.domain.model.procurement.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支払方法区分
 */
@AllArgsConstructor
@Getter
public enum PurchasePaymentMethodType {
    現金(1, "現金"),
    小切手(2, "小切手"),
    手形(3, "手形"),
    振込(4, "振込"),
    相殺(5, "相殺"),
    その他(9, "その他");

    private final Integer code;
    private final String name;

    /**
     * コードから支払方法区分を取得する
     *
     * @param code 支払方法区分コード
     * @return 支払方法区分
     */
    public static PurchasePaymentMethodType fromCode(Integer code) {
        if (code == null) {
            return 振込;
        }

        for (PurchasePaymentMethodType type : PurchasePaymentMethodType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }

        return 振込;
    }
}
