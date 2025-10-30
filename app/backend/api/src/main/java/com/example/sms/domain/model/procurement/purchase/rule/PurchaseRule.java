package com.example.sms.domain.model.procurement.purchase.rule;

import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.model.procurement.purchase.PurchaseLine;

/**
 * 仕入ルール
 */
public abstract class PurchaseRule {
    public abstract boolean isSatisfiedBy(Purchase purchase);
    public abstract boolean isSatisfiedBy(PurchaseLine purchaseLine);
    public abstract boolean isSatisfiedBy(Purchase purchase, PurchaseLine purchaseLine);
}
