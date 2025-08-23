package com.example.sms.domain.model.procurement.purchase.rule;

import com.example.sms.domain.model.procurement.purchase.PurchaseOrder;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderLine;
import com.example.sms.domain.type.money.Money;

/**
 * 発注金額ルール
 * 発注金額が500万円を超過している場合は要確認とする
 */
public class PurchaseOrderAmountRule extends PurchaseOrderRule {
    private static final Money THRESHOLD = Money.of(5000000);

    @Override
    public boolean isSatisfiedBy(PurchaseOrder purchaseOrder) {
        return (purchaseOrder.getTotalPurchaseAmount().isGreaterThan(THRESHOLD));
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