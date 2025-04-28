package com.example.sms.domain.model.order;

import lombok.Getter;

/**
 * 消費税率種別
 */
@Getter
public enum TaxRateType {
    標準税率(10, "通常の消費税率"),
    軽減税率(8, "軽減税率（食品など）");

    private final Integer rate;
    private final String description;

    TaxRateType(Integer rate, String description) {
        this.rate = rate;
        this.description = description;
    }

    public static TaxRateType of(Integer rate) {
        for (TaxRateType taxRateType : TaxRateType.values()) {
            if (taxRateType.getRate().equals(rate)) {
                return taxRateType;
            }
        }
        throw new IllegalArgumentException("不正な消費税率です");
    }
}
