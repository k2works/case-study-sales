package com.example.sms.domain.model.procurement.receipt.rule;

import com.example.sms.domain.model.procurement.receipt.Purchase;
import com.example.sms.domain.model.procurement.receipt.PurchaseLine;

/**
 * 仕入ルール
 */
public abstract class PurchaseRule {
    public abstract boolean isSatisfiedBy(Purchase purchase);
    public abstract boolean isSatisfiedBy(PurchaseLine purchaseLine);
    public abstract boolean isSatisfiedBy(Purchase purchase, PurchaseLine purchaseLine);
}
