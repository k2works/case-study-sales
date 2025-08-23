package com.example.sms.domain.model.procurement.purchase.order.rule;

import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrderLine;

/**
 * 発注納期ルール
 * 指定納期が発注日より前の場合は無効とする
 */
public class PurchaseOrderDeliveryRule extends PurchaseOrderRule {

    @Override
    public boolean isSatisfiedBy(PurchaseOrder purchaseOrder) {
        return purchaseOrder.getDesignatedDeliveryDate().getValue()
                .isBefore(purchaseOrder.getPurchaseOrderDate().getValue());
    }

    @Override
    public boolean isSatisfiedBy(PurchaseOrderLine purchaseOrderLine) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(PurchaseOrder purchaseOrder, PurchaseOrderLine purchaseOrderLine) {
        return false;
    }
}