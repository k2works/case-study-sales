package com.example.sms.domain.model.order;

import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.Value;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 販売価格
 */
@Value
public class SalesAmount {
    Money value;
    Money salesUnitPrice; // 販売単価
    Quantity orderQuantity; // 受注数量

    public SalesAmount (Money salesUnitPrice, Quantity orderQuantity) {
        notNull(salesUnitPrice, "販売単価は必須です。");
        notNull(orderQuantity, "受注数量は必須です。");

        this.salesUnitPrice = salesUnitPrice;
        this.orderQuantity = orderQuantity;

        this.value = salesUnitPrice.multiply(orderQuantity);
    }

    public static SalesAmount of(Money salesUnitPrice, Quantity orderQuantity) {
        return new SalesAmount(salesUnitPrice, orderQuantity);
    }
}
