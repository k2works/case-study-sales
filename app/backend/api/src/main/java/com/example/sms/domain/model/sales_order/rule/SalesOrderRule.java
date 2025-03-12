package com.example.sms.domain.model.sales_order.rule;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;

/**
 * 受注ルール
 */
public abstract class SalesOrderRule {
    public abstract boolean isSatisfiedBy(SalesOrder salesOrder);
    public abstract boolean isSatisfiedBy(SalesOrderLine salesOrderLine);
    public abstract boolean isSatisfiedBy(SalesOrder salesOrder, SalesOrderLine salesOrderLine);
}
