package com.example.sms.domain.type;

/**
 * 畜産区分
 */
public enum LiveStockType {
    牛肉("01"), 豚肉("02"), まぐろ("03"), えび("04"), その他("99");

    private final String code;

    LiveStockType(String code) {
        this.code = code;
    }

    public static LiveStockType fromCode(String code) {
        for (LiveStockType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("畜産区分未登録:" + code);
    }
}
