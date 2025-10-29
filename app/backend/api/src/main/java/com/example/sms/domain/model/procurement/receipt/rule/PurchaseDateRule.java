package com.example.sms.domain.model.procurement.receipt.rule;

import com.example.sms.domain.model.procurement.receipt.Purchase;
import com.example.sms.domain.model.procurement.receipt.PurchaseLine;

import java.time.LocalDateTime;

/**
 * 仕入日ルール
 * 仕入日が未来日の場合は無効とする
 */
public class PurchaseDateRule extends PurchaseRule {

    @Override
    public boolean isSatisfiedBy(Purchase purchase) {
        LocalDateTime now = LocalDateTime.now();
        return purchase.getPurchaseDate().getValue().isAfter(now);
    }

    @Override
    public boolean isSatisfiedBy(PurchaseLine purchaseLine) {
        return false;
    }

    @Override
    public boolean isSatisfiedBy(Purchase purchase, PurchaseLine purchaseLine) {
        return false;
    }
}
