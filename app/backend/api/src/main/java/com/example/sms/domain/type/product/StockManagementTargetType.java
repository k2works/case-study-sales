package com.example.sms.domain.type.product;

import lombok.Getter;

/**
 * 在庫管理対象区分
 */
@Getter
public enum StockManagementTargetType {
    対象外(0), 対象(1);

    private final Integer code;

    StockManagementTargetType(Integer code) {
        this.code = code;
    }

    public static StockManagementTargetType fromCode(Integer code) {
        for (StockManagementTargetType stockManagementTargetType : StockManagementTargetType.values()) {
            if (stockManagementTargetType.code.equals(code)) {
                return stockManagementTargetType;
            }
        }
        throw new IllegalArgumentException("在庫管理対象区分未登録:" + code);
    }
}
