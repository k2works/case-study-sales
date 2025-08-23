package com.example.sms.domain.model.procurement.purchase.order.rule;

import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrderLine;

import java.time.LocalDateTime;

/**
 * 発注納期超過ルール
 * 指定納期が現在日時を超過している場合は遅延とする
 */
public class PurchaseOrderDeliveryOverDueRule extends PurchaseOrderRule {

    @Override
    public boolean isSatisfiedBy(PurchaseOrder purchaseOrder) {
        LocalDateTime now = LocalDateTime.now();
        return purchaseOrder.getDesignatedDeliveryDate().getValue().isBefore(now);
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