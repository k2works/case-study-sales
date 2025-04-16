package com.example.sms.domain.model.sales;

import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 請求番号
 */
@Value
@NoArgsConstructor(force = true)
public class BillingNumber {
    String value;

    public BillingNumber(String billingNumber) {
        notNull(billingNumber, "請求番号は必須です");

        this.value = billingNumber;
    }

    public static BillingNumber of(String billingNumber) {
        return new BillingNumber(billingNumber);
    }
}
