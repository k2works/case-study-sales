package com.example.sms.domain.type.product;

/**
 * 品目区分
 */
public enum ItemType {
    食肉("01"), 水産物("02"), その他("99");

    private final String code;

    ItemType(String code) {
        this.code = code;
    }

    public static ItemType fromCode(String code) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.code.equals(code)) {
                return itemType;
            }
        }
        throw new IllegalArgumentException("品目区分未登録:" + code);
    }
}
