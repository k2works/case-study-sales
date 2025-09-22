package com.example.sms.domain.model.inventory;

import lombok.Value;

/**
 * ロット番号
 */
@Value
public class LotNumber {
    String value;

    public static LotNumber of(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("ロット番号は必須です");
        }
        if (value.length() > 20) {
            throw new IllegalArgumentException("ロット番号は20文字以下である必要があります");
        }
        // ロット番号の形式チェック（英数字とハイフンのみ許可）
        if (!value.matches("^[A-Za-z0-9\\-]+$")) {
            throw new IllegalArgumentException("ロット番号は英数字とハイフンのみ使用できます");
        }
        return new LotNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}