package com.example.sms.domain.model.procurement.payment;

import lombok.Value;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 支払方法区分
 */
@Value(staticConstructor = "of")
public class PurchasePaymentMethodType {
    Integer value;

    private PurchasePaymentMethodType(Integer value) {
        notNull(value, "支払方法区分は必須です");
        this.value = value;
    }
}
