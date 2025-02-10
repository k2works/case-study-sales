package com.example.sms.domain.model.master.partner.invoice;

import com.example.sms.domain.BusinessException;
import lombok.Getter;

/**
 * 支払日
 */
@Getter
public enum PaymentDay {
    十日(10),
    二十日(20),
    末日(99);

    private final Integer value;

    PaymentDay(Integer value) {
        this.value = value;
    }

    public static PaymentDay fromCode(Integer value) {
        for (PaymentDay paymentDay : PaymentDay.values()) {
            if (paymentDay.value.equals(value)) {
                return paymentDay;
            }
        }
        throw new BusinessException("Illegal PaymentDay value: " + value);
    }
}
