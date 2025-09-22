package com.example.sms.domain.model.inventory;

import lombok.Getter;

/**
 * 在庫区分
 */
@Getter
public enum StockCategory {
    通常在庫("1", "通常の在庫"),
    安全在庫("2", "安全在庫（最低保持数量）"),
    廃棄予定("3", "廃棄予定在庫");

    private final String code;
    private final String description;

    StockCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static StockCategory of(String code) {
        for (StockCategory stockCategory : StockCategory.values()) {
            if (stockCategory.getCode().equals(code)) {
                return stockCategory;
            }
        }
        throw new IllegalArgumentException("不正な在庫区分です: " + code);
    }
}