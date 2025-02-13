package com.example.sms.domain.model.master.region;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.*;

/**
 * 地域コード
 */
@Value
@NoArgsConstructor(force = true)
public class RegionCode {
    String value;

    public RegionCode(String value) {
        notNull(value, "地域コードは必須です");
        isTrue(
                value.length() == 4,
                "地域コードは4文字である必要があります: %s",
                value
        );
        matchesPattern(
                value,
                "R\\d{3}",
                "地域コードはR + 3桁の数字である必要があります: %s",
                value
        );

        this.value = value;
    }

    public static RegionCode of(String value) {
        return new RegionCode(value);
    }
}