package com.example.sms.domain.model.master.partner;

import lombok.Getter;

/**
 * 雑区分
 */
@Getter
public enum MiscellaneousType {
    対象外(0),
    対象(1);

    private final int code;

    MiscellaneousType(int code) {
        this.code = code;
    }

    public static MiscellaneousType fromCode(int code) {
        for (MiscellaneousType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("雑区分に該当する値が存在しません。");
    }

    public static Integer getCodeByName(String s) {
        for (MiscellaneousType value : values()) {
            if (value.name().equals(s)) {
                return value.code;
            }
        }
        throw new IllegalArgumentException("雑区分に該当する値が存在しません。");
    }
}
