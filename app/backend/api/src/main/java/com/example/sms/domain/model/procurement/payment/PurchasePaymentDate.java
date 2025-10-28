package com.example.sms.domain.model.procurement.payment;

import lombok.Value;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 支払日
 */
@Value(staticConstructor = "of")
public class PurchasePaymentDate {
    Integer value;

    private PurchasePaymentDate(Integer value) {
        notNull(value, "支払日は必須です");
        this.value = value;
    }
}
