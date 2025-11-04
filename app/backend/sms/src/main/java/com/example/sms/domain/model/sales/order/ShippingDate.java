package com.example.sms.domain.model.sales.order;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 出荷日
 */
@Value
@NoArgsConstructor(force = true)
public class ShippingDate {
    LocalDateTime value;

    public ShippingDate(LocalDateTime shippingDate) {
        notNull(shippingDate, "出荷日は必須です");

        this.value = shippingDate;
    }

    public static ShippingDate of(LocalDateTime shippingDate) {
        if (shippingDate == null) {
            return null;
        }
        return new ShippingDate(shippingDate);
    }
}