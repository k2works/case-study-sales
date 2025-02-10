package com.example.sms.domain.model.master.partner.invoice;

import com.example.sms.domain.BusinessException;
import lombok.Getter;

/**
 * 支払月
 */
@Getter
public enum PaymentMonth {
    当月(0),
    翌月(1),
    翌々月(2);

    private final Integer value;

    PaymentMonth(Integer value) {
        this.value = value;
    }

    public static PaymentMonth fromCode(Integer value) {
        for (PaymentMonth paymentMonth : PaymentMonth.values()) {
            if (paymentMonth.value.equals(value)) {
                return paymentMonth;
            }
        }
        throw new BusinessException("Illegal PaymentMonth value: " + value);
    }
}
