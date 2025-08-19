package com.example.sms.domain.model.sales.purchase.order.rule;

import com.example.sms.domain.model.sales.purchase.order.PurchaseOrder;
import com.example.sms.domain.model.sales.purchase.order.PurchaseOrderLine;

/**
 * 発注ルール
 */
public abstract class PurchaseOrderRule {
    public abstract boolean isSatisfiedBy(PurchaseOrder purchaseOrder);
    public abstract boolean isSatisfiedBy(PurchaseOrderLine purchaseOrderLine);
    public abstract boolean isSatisfiedBy(PurchaseOrder purchaseOrder, PurchaseOrderLine purchaseOrderLine);
}