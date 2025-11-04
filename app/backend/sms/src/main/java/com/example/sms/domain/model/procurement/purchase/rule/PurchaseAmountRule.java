package com.example.sms.domain.model.procurement.purchase.rule;

import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.model.procurement.purchase.PurchaseLine;
import com.example.sms.domain.type.money.Money;

/**
 * 仕入金額ルール
 * 仕入金額が500万円を超過している場合は要確認とする
 */
public class PurchaseAmountRule extends PurchaseRule {
    private static final Money THRESHOLD = Money.of(5000000);

    @Override
    public boolean isSatisfiedBy(Purchase purchase) {
        return (purchase.getTotalPurchaseAmount().isGreaterThan(THRESHOLD));
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
