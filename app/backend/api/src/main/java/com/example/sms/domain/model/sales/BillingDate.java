package com.example.sms.domain.model.sales;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static io.jsonwebtoken.lang.Assert.notNull;

/**
 * 請求日
 */
@Value
@NoArgsConstructor(force = true)
public class BillingDate {
    LocalDateTime value;

    public BillingDate(LocalDateTime billingDate) {
        notNull(billingDate, "請求日は必須です");

        this.value = billingDate;
    }

    public static BillingDate of(LocalDateTime billingDate) {
        return new BillingDate(billingDate);
    }
}
