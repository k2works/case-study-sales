package com.example.sms.domain.model.master.warehouse;

import lombok.Value;

@Value
public class WarehouseCode {
    String value;

    public static WarehouseCode of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("倉庫コードは必須です");
        }
        return new WarehouseCode(value);
    }
}