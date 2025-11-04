package com.example.sms.domain.model.sales.order;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 納期
 */
@Value
@NoArgsConstructor(force = true)
public class DeliveryDate {
    LocalDateTime value;

    public DeliveryDate(LocalDateTime deliveryDate) {
        notNull(deliveryDate, "納期は必須です");
        isTrue(!deliveryDate.isAfter(LocalDateTime.now().plusYears(1)), "納期は１年以内に設定してください");

        this.value = deliveryDate;
    }

    public static DeliveryDate of(LocalDateTime deliveryDate) {
        return new DeliveryDate(deliveryDate);
    }
}
