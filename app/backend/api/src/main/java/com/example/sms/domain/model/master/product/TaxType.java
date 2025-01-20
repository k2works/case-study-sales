package com.example.sms.domain.model.master.product;

import lombok.Getter;

/**
 * 税区分
 */
@Getter
public enum TaxType {
    外税(1), 内税(2), 非課税(3), その他(9);

    private final Integer code;

    TaxType(Integer code) {
        this.code = code;
    }

    public static TaxType fromCode(Integer code) {
        for (TaxType taxType : TaxType.values()) {
            if (taxType.code.equals(code)) {
                return taxType;
            }
        }
        throw new IllegalArgumentException("税区分未登録:" + code);
    }
}
