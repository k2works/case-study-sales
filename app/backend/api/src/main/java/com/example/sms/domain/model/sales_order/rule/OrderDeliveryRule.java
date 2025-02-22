package com.example.sms.domain.model.sales_order.rule;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;

/**
 * 受注納期ルール
 */
public class OrderDeliveryRule extends SalesOrderRule {
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
        return salesOrderLine.getDeliveryDate().getValue().isBefore(salesOrder.getOrderDate().getValue());
    }
}
