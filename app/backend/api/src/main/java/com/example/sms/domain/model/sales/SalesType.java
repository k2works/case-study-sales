package com.example.sms.domain.model.sales;

import lombok.Getter;

/**
 * 売上区分
 */
@Getter
public enum SalesType {
    現金(0), 掛(1), その他(2);

    private final Integer code;

    SalesType(Integer code) {
        this.code = code;
    }

    public static Integer getCodeByName(String name) {
        for (SalesType salesType : SalesType.values()) {
            if (salesType.name().equals(name)) {
                return salesType.getCode();
            }
        }
        throw new IllegalArgumentException("売上区分未登録:" + name);
    }

    public static SalesType fromCode(Integer code) {
        for (SalesType salesType : SalesType.values()) {
            if (salesType.code.equals(code)) {
                return salesType;
            }
        }
        throw new IllegalArgumentException("売上区分未登録:" + code);
    }
}
