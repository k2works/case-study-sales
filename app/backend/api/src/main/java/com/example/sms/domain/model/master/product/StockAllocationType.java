package com.example.sms.domain.model.master.product;

import lombok.Getter;

/**
 * 在庫引当区分
 */
@Getter
public enum StockAllocationType {
    未引当(0), 引当済(1);

    private final Integer code;

    StockAllocationType(Integer code) {
        this.code = code;
    }

    public static StockAllocationType fromCode(Integer code) {
        for (StockAllocationType stockAllocationType : StockAllocationType.values()) {
            if (stockAllocationType.code.equals(code)) {
                return stockAllocationType;
            }
        }
        throw new IllegalArgumentException("在庫引当区分未登録:" + code);
    }
}
