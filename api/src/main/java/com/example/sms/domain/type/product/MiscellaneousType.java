package com.example.sms.domain.type.product;

import lombok.Getter;

/**
 * 雑区分
 */
@Getter
public enum MiscellaneousType {
    適用(1), 適用外(0);

    private final Integer code;

    MiscellaneousType(Integer code) {
        this.code = code;
    }

    public static MiscellaneousType fromCode(Integer code) {
        for (MiscellaneousType miscellaneousType : MiscellaneousType.values()) {
            if (miscellaneousType.code.equals(code)) {
                return miscellaneousType;
            }
        }
        throw new IllegalArgumentException("雑区分未登録:" + code);
    }
}
