package com.example.sms.domain.model.sales_order.rule;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;

import java.time.LocalDateTime;

/**
 * 受注納期超過ルール
 */
public class OrderDeliveryOverDueRule extends SalesOrderRule {
    @Override
    public boolean isSatisfiedBy(SalesOrder salesOrder) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(SalesOrderLine salesOrderLine) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(SalesOrder salesOrder, SalesOrderLine salesOrderLine) {
        return salesOrderLine.getDeliveryDate().getValue().isBefore(LocalDateTime.now());
    }
}
