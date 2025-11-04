package com.example.sms.domain.model.sales.order;


import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 希望納期
 */
@Value
@NoArgsConstructor(force = true)
public class DesiredDeliveryDate {
    LocalDateTime value;

    public DesiredDeliveryDate(LocalDateTime desiredDeliveryDate) {
        this.value = desiredDeliveryDate;
    }

    public static DesiredDeliveryDate of(LocalDateTime desiredDeliveryDate) {
        return new DesiredDeliveryDate(desiredDeliveryDate);
    }
}
