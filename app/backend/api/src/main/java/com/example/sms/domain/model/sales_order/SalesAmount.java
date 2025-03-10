package com.example.sms.domain.model.sales_order;

import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.Value;

/**
 * 販売価格
 */
@Value
public class SalesAmount {
    Money value;
    Money salesUnitPrice; // 販売単価
    Quantity orderQuantity; // 受注数量

    public SalesAmount (Money salesUnitPrice, Quantity orderQuantity) {
        this.salesUnitPrice = salesUnitPrice;
        this.orderQuantity = orderQuantity;

        this.value = salesUnitPrice.multiply(orderQuantity);
    }

    public static SalesAmount of(Money salesUnitPrice, Quantity orderQuantity) {
        return new SalesAmount(salesUnitPrice, orderQuantity);
    }
}
