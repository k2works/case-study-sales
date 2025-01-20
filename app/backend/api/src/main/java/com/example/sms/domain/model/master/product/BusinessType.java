package com.example.sms.domain.model.master.product;

/**
 * 事業区分
 */
public enum BusinessType {
    生鮮食料品("1"), 缶詰("2"), その他("9");

    private final String code;

    BusinessType(String code) {
        this.code = code;
    }

    public static BusinessType fromCode(String code) {
        for (BusinessType businessType : BusinessType.values()) {
            if (businessType.code.equals(code)) {
                return businessType;
            }
        }
        throw new IllegalArgumentException("事業区分未登録:" + code);
    }
}
