package com.example.sms.domain.model.master.warehouse;

import lombok.Value;

/**
 * 倉庫コード
 */
@Value
public class WarehouseCode {
    String value;

    public static WarehouseCode of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("倉庫コードは必須です");
        }
        if (!isValidFormat(value)) {
            throw new IllegalArgumentException("倉庫コードは先頭がW、残り2文字が数字の形式である必要があります（例: W01）");
        }
        return new WarehouseCode(value);
    }

    private static boolean isValidFormat(String value) {
        if (value.length() != 3) {
            return false;
        }
        if (value.charAt(0) != 'W') {
            return false;
        }
        return Character.isDigit(value.charAt(1)) && Character.isDigit(value.charAt(2));
    }
}