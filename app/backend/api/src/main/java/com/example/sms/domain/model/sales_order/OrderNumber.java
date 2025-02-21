package com.example.sms.domain.model.sales_order;


import lombok.NoArgsConstructor;
import lombok.Value;

import static org.apache.commons.lang3.Validate.*;

/**
 * 受注番号
 */
@Value
@NoArgsConstructor(force = true)
public class OrderNumber {
    String value;

    public OrderNumber(String orderNumber) {
        notNull(orderNumber, "受注番号は必須です");
        isTrue(!orderNumber.startsWith("0"), "注文番号は先頭が0であってはいけません");
        matchesPattern(orderNumber, "[0-9]{10}", "注文番号は10桁の数字である必要があります");
        this.value = orderNumber;
    }

    public static OrderNumber of(String orderNumber) {
        return new OrderNumber(orderNumber);
    }
}
