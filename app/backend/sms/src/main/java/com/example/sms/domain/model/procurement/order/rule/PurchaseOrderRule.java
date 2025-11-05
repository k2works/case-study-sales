package com.example.sms.domain.model.procurement.order.rule;

import com.example.sms.domain.model.procurement.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.order.PurchaseOrderLine;

/**
 * 発注ルール
 */
public abstract class PurchaseOrderRule {
    public abstract boolean isSatisfiedBy(PurchaseOrder purchaseOrder);
    public abstract boolean isSatisfiedBy(PurchaseOrderLine purchaseOrderLine);
    public abstract boolean isSatisfiedBy(PurchaseOrder purchaseOrder, PurchaseOrderLine purchaseOrderLine);
}