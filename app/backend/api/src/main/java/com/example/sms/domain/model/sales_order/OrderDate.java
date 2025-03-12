package com.example.sms.domain.model.sales_order;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 受注日
 */
@Value
@NoArgsConstructor(force = true)
public class OrderDate {
    LocalDateTime value;

    public OrderDate(LocalDateTime orderDate) {
        notNull(orderDate, "受注日は必須です");

        this.value = orderDate;
    }

    public static OrderDate of(LocalDateTime orderDate) {
        return new OrderDate(orderDate);
    }
}
