package com.example.sms.domain.model.master.partner;

import lombok.Getter;

/**
 * 取引禁止フラグ
 */
@Getter
public enum TradeProhibitedFlag {
    OFF(0),
    ON(1);

    private final int value;

    TradeProhibitedFlag(int value) {
        this.value = value;
    }

    public static TradeProhibitedFlag fromCode(Integer tradeProhibitedFlag) {
        for (TradeProhibitedFlag flag : TradeProhibitedFlag.values()) {
            if (flag.value == tradeProhibitedFlag) {
                return flag;
            }
        }
        throw new IllegalArgumentException("取引禁止フラグ未登録:" + tradeProhibitedFlag);
    }

}
