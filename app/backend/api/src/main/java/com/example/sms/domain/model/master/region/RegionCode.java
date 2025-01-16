package com.example.sms.domain.model.master.region;

import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * 地域コード
 */
@Value
@NoArgsConstructor(force = true)
public class RegionCode {
    String value;

    public RegionCode(String value) {
        if (value == null) {
            throw new IllegalArgumentException("地域コードは必須です");
        }
        if (value.length() != 4) {
            throw new IllegalArgumentException("地域コードは4文字である必要があります");
        }
        if (!value.matches("R\\d{3}")) {
            throw new IllegalArgumentException("地域コードはR + 3桁の数字である必要があります");
        }

        this.value = value;
    }

    public static RegionCode of(String value) {
        return new RegionCode(value);
    }
}
