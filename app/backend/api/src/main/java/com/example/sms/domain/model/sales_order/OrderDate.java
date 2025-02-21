package com.example.sms.domain.model.sales_order;

import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 受注日
 */
@Value
@NoArgsConstructor(force = true)
public class OrderDate {
    LocalDateTime value;

    public OrderDate(LocalDateTime orderDate) {
        this.value = orderDate;
    }

    public static OrderDate of(LocalDateTime orderDate) {
        return new OrderDate(orderDate);
    }
}
