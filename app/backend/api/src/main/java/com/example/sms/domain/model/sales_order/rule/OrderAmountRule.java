package com.example.sms.domain.model.sales_order.rule;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.type.money.Money;

/**
 * 受注金額ルール
 */
public class OrderAmountRule extends SalesOrderRule {
    private static final Money THRESHOLD = Money.of(1000000);

    @Override
    public boolean isSatisfiedBy(SalesOrder salesOrder) {
        return (salesOrder.getTotalOrderAmount().isGreaterThan(THRESHOLD));
    }

    @Override
    public boolean isSatisfiedBy(SalesOrderLine salesOrderLine) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(SalesOrder salesOrder, SalesOrderLine salesOrderLine) {
        return false;
    }
}
