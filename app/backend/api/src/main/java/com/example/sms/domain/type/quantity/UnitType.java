package com.example.sms.domain.type.quantity;

/**
 * 単位区分
 */
public enum UnitType {
    個("1"), グラム("2"), ミリリットル("3"), パック("4"), 本("5"), セット("6"), その他("9");

    private final String code;

    UnitType(String code) {
        this.code = code;
    }

    public static UnitType fromCode(String code) {
        for (UnitType unitType : UnitType.values()) {
            if (unitType.code.equals(code)) {
                return unitType;
            }
        }
        throw new IllegalArgumentException("単位区分未登録:" + code);
    }
}
