package com.example.sms.domain.service.procurement.purchase;

import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.model.procurement.purchase.PurchaseList;
import com.example.sms.domain.model.procurement.purchase.rule.*;
import com.example.sms.domain.type.money.Money;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 仕入ドメインサービス
 */
@Service
public class PurchaseDomainService {

    /**
     * 仕入金額合計計算
     */
    public Money calculateTotalPurchaseAmount(Purchase purchase) {
        if (purchase.getPurchaseLines() == null || purchase.getPurchaseLines().isEmpty()) {
            return Money.of(0);
        }

        return purchase.calcTotalPurchaseAmount();
    }

    /**
     * 仕入消費税合計計算
     */
    public Money calculateTotalConsumptionTax(Purchase purchase) {
        if (purchase.getPurchaseLines() == null || purchase.getPurchaseLines().isEmpty()) {
            return Money.of(0);
        }

        return purchase.calcTotalConsumptionTax();
    }

    /**
     * 仕入ルールチェック
     */
    public PurchaseRuleCheckList checkRule(PurchaseList purchases) {
        List<Map<String, String>> checkList = new ArrayList<>();

        List<Purchase> purchaseList = purchases.asList();
        PurchaseRule purchaseAmountRule = new PurchaseAmountRule();
        PurchaseRule purchaseDateRule = new PurchaseDateRule();

        BiConsumer<String, String> addCheck = (purchaseNumber, message) -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(purchaseNumber, message);
            checkList.add(errorMap);
        };

        purchaseList.forEach(purchase -> {
            // 仕入金額ルールチェック
            if (purchaseAmountRule.isSatisfiedBy(purchase)) {
                addCheck.accept(purchase.getPurchaseNumber().getValue(), "仕入金額が500万円を超えています。");
            }

            // 仕入日ルールチェック
            if (purchaseDateRule.isSatisfiedBy(purchase)) {
                addCheck.accept(purchase.getPurchaseNumber().getValue(), "仕入日が未来日です。");
            }
        });

        return new PurchaseRuleCheckList(checkList);
    }
}
