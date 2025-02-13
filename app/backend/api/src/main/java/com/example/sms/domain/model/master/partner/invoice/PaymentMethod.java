package com.example.sms.domain.model.master.partner.invoice;

import lombok.Getter;

/**
 * 支払方法
 */
@Getter
public enum PaymentMethod {
    振込(1),
    手形(2);

    private final Integer value;

    PaymentMethod(Integer value) {
        this.value = value;
    }

    public static PaymentMethod fromCode(Integer value) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.value.equals(value)) {
                return paymentMethod;
            }
        }
        throw new IllegalArgumentException("Illegal PaymentMethod value: " + value);
    }
}
