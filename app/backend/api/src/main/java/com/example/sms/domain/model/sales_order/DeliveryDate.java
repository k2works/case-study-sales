package com.example.sms.domain.model.sales_order;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 納期
 */
@Value
@NoArgsConstructor(force = true)
public class DeliveryDate {
    LocalDateTime value;

    public DeliveryDate(LocalDateTime deliveryDate) {
        this.value = deliveryDate;
    }

    public static DeliveryDate of(LocalDateTime deliveryDate) {
        return new DeliveryDate(deliveryDate);
    }
}
