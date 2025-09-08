package com.example.sms.domain.model.procurement.purchase.rule;

import com.example.sms.domain.model.procurement.purchase.PurchaseOrder;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderLine;

/**
 * 発注ルール
 */
public abstract class PurchaseOrderRule {
    public abstract boolean isSatisfiedBy(PurchaseOrder purchaseOrder);
    public abstract boolean isSatisfiedBy(PurchaseOrderLine purchaseOrderLine);
    public abstract boolean isSatisfiedBy(PurchaseOrder purchaseOrder, PurchaseOrderLine purchaseOrderLine);
}