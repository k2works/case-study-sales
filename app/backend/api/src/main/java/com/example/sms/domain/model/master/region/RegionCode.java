package com.example.sms.domain.model.master.region;

import com.example.sms.domain.BusinessException;
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
            throw new BusinessException("地域コードは必須です");
        }
        if (value.length() != 4) {
            throw new BusinessException("地域コードは4文字である必要があります");
        }
        if (!value.matches("R\\d{3}")) {
            throw new BusinessException("地域コードはR + 3桁の数字である必要があります");
        }

        this.value = value;
    }

    public static RegionCode of(String value) {
        return new RegionCode(value);
    }
}
