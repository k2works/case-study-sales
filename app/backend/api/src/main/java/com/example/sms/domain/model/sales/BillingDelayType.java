package com.example.sms.domain.model.sales;

import lombok.Getter;

/**
 * 請求遅延区分
 */
@Getter
public enum BillingDelayType {
    未遅延(0), 遅延(1);

    private final Integer code;

    BillingDelayType(Integer code) {
        this.code = code;
    }

    public static Integer getCodeByName(String name) {
        for (BillingDelayType billingDelayType : BillingDelayType.values()) {
            if (billingDelayType.name().equals(name)) {
                return billingDelayType.getCode();
            }
        }
        throw new IllegalArgumentException("請求遅延区分未登録:" + name);
    }

    public static BillingDelayType fromCode(Integer code) {
        for (BillingDelayType billingDelayType : BillingDelayType.values()) {
            if (billingDelayType.code.equals(code)) {
                return billingDelayType;
            }
        }
        throw new IllegalArgumentException("請求遅延区分未登録:" + code);
    }
}
