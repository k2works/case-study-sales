package com.example.sms.domain.type.product;

import lombok.Getter;

/**
 * 雑区分
 */
@Getter
public enum MiscellaneousType {
    適用外(0), 適用(1);

    private final Integer code;

    MiscellaneousType(Integer code) {
        this.code = code;
    }

    public static Integer getCodeByName(String name) {
        for (MiscellaneousType miscellaneousType : MiscellaneousType.values()) {
            if (miscellaneousType.name().equals(name)) {
                return miscellaneousType.getCode();
            }
        }
        throw new IllegalArgumentException("雑区分未登録:" + name);
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
