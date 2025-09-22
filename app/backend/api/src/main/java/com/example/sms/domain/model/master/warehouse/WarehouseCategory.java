package com.example.sms.domain.model.master.warehouse;

import lombok.Getter;

/**
 * 倉庫区分
 * N:通常倉庫, C:得意先, S:仕入先, D:部門倉庫, P:製品倉庫, M:原材料倉庫
 */
@Getter
public enum WarehouseCategory {
    通常倉庫("N", "通常倉庫"),
    得意先("C", "得意先"),
    仕入先("S", "仕入先"),
    部門倉庫("D", "部門倉庫"),
    製品倉庫("P", "製品倉庫"),
    原材料倉庫("M", "原材料倉庫");

    private final String code;
    private final String description;

    WarehouseCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static WarehouseCategory of(String code) {
        if (code == null) {
            return null;
        }
        for (WarehouseCategory category : WarehouseCategory.values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("不正な倉庫区分です: " + code);
    }
}